package com.andrewlevada.carephoneserver;

import com.andrewlevada.carephoneserver.logic.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.*;

@Repository
public class Database {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	// Users

	public void addUser(String uid) {
		jdbcTemplate.update("INSERT INTO public.\"CaredUsers\" (uid, caretaker_uid, is_whitelist_active, is_pro) VALUES (?, NULL, TRUE, FALSE)",
			uid);
	}

	public Boolean hasUser(String uid) {
		List<String> list = jdbcTemplate.query("SELECT uid FROM public.\"CaredUsers\" WHERE uid = ?",
			(resultSet, i) -> resultSet.getString("uid"), uid);

		return list.size() == 1;
	}

	public void makeUserPro(String uid) {
		jdbcTemplate.update("UPDATE public.\"CaredUsers\" SET is_pro = ? WHERE uid = ?", true, uid);
	}

	// Whitelist

	public List<PhoneNumber> getWhitelist(String uid) {
		return jdbcTemplate.query("SELECT phone_number, label FROM public.\"WhitelistRecords\" WHERE uid = ?", new PhoneNumber.Mapper(), uid);
	}

	public void addWhitelistRecord(String uid, PhoneNumber phoneNumber) {
		jdbcTemplate.update("INSERT INTO public.\"WhitelistRecords\" (uid, phone_number, label) VALUES (?, ?, ?)",
			uid, phoneNumber.phone, phoneNumber.label);
	}

	public void deleteWhitelistRecord(String uid, String phone) {
		jdbcTemplate.update("DELETE FROM public.\"WhitelistRecords\" WHERE uid = ? AND phone_number = ?",
			uid, phone);
	}

	public void editWhitelistRecord(String uid, String prevPhone, PhoneNumber phoneNumber) {
		jdbcTemplate.update("UPDATE public.\"WhitelistRecords\" SET phone_number = ?, label = ? WHERE uid = ? AND phone_number = ?",
			phoneNumber.phone, phoneNumber.label, uid, prevPhone);
	}

	// Whitelist State

	public Boolean getWhitelistState(String uid) {
		RowMapper<Boolean> mapper = (resultSet, i) -> resultSet.getBoolean("is_whitelist_active");

		List<Boolean> list = jdbcTemplate.query("SELECT is_whitelist_active FROM public.\"CaredUsers\" WHERE uid = ?",
			mapper, uid);

		if (list.size() != 0) return list.get(0);
		else return false;
	}

	public void setWhitelistState(String uid, boolean state) {
		jdbcTemplate.update("UPDATE public.\"CaredUsers\" SET is_whitelist_active = ? WHERE uid = ?",
			state, uid);
	}

	// Log

	public List<LogRecord> getAllLogRecords(String uid) {
		return jdbcTemplate.query("SELECT * FROM public.\"LogRecords\" WHERE uid = ?", new LogRecord.Mapper(), uid);
	}

	public List<LogRecord> getLimitedLogRecords(String uid, int limit, int offset) {
		return jdbcTemplate.query("SELECT * FROM public.\"LogRecords\" WHERE uid = ? LIMIT ? OFFSET ?", new LogRecord.Mapper(), uid, limit, offset);
	}

	public void addLogRecord(String uid, String phoneNumber, long startTimestamp, int secondsDuration, int type) {
		jdbcTemplate.update("INSERT INTO public.\"LogRecords\" (uid, phone_number, start_timestamp, seconds_duration, type) VALUES (?, ?, ?, ?, ?)",
			uid, phoneNumber, startTimestamp, secondsDuration, type);
	}

	// Statistics

	public Pair<List<String>, List<Integer>> getTopPhonesByMinutes(String uid, int limit) {
		List<LabeledNumber> list = jdbcTemplate.query("SELECT phone_number, COALESCE(SUM(seconds_duration), 0) AS seconds FROM public.\"LogRecords\" WHERE uid = ? GROUP BY phone_number LIMIT ?",
			(resultSet, i) -> new LabeledNumber(resultSet.getString("phone_number"), resultSet.getInt("seconds") / 60),
			uid, limit);

		Pair<List<String>, List<Integer>> pair = new Pair<>(new ArrayList<>(), new ArrayList<>());
		for (LabeledNumber labeledNumber: list) {
			pair.first.add(labeledNumber.label);
			pair.second.add(labeledNumber.number);
		}

		return pair;
	}

	public Integer getTalkMinutesByPeriod(String uid, long period) {
		List<Integer> list = jdbcTemplate.query("SELECT COALESCE(SUM(seconds_duration), 0) AS seconds FROM public.\"LogRecords\" WHERE uid = ? AND start_timestamp > ?",
			(resultSet, i) -> resultSet.getInt("seconds") / 60,
			uid, System.currentTimeMillis() - period);

		if (list.size() != 1) return 0;
		else return list.get(0);
	}

	public Integer getTalkMinutes(String uid) {
		List<Integer> list = jdbcTemplate.query("SELECT COALESCE(SUM(seconds_duration), 0) AS seconds FROM public.\"LogRecords\" WHERE uid = ?",
			(resultSet, i) -> resultSet.getInt("seconds") / 60, uid);

		if (list.size() != 1) return 0;
		else return list.get(0);
	}

	// Cared List

	public List<CaredUser> getCaredList(String uid) {
		return jdbcTemplate.query("SELECT uid FROM public.\"CaredUsers\" WHERE caretaker_uid = ?",
			new CaredUser.Mapper(), uid);
	}

	public List<CaredUser2> getCaredList2(String uid) {
		return jdbcTemplate.query("SELECT uid, is_pro FROM public.\"CaredUsers\" WHERE caretaker_uid = ?",
			new CaredUser2.Mapper(), uid);
	}

	// Linking

	public String addLinkRequest(String uid) {
		List<LinkRequest> list = jdbcTemplate.query("SELECT * FROM public.\"LinkRequests\" WHERE uid = ?",
			(resultSet, i) -> new LinkRequest(resultSet.getString("uid"), resultSet.getString("code"), resultSet.getLong("end_timestamp")), uid);

		if (list.size() != 0) {
			if (list.get(0).endTime > System.currentTimeMillis()) return list.get(0).code;
			else {
				deleteLinkRequestByUid(uid);
				return addLinkRequest(uid);
			}
		}

		long endTime = System.currentTimeMillis() + 1000 * 60 * 10;
		String code = DataProcessing.generateLinkCode(this);
		jdbcTemplate.update("INSERT INTO public.\"LinkRequests\" (uid, code, end_timestamp) VALUES (?, ?, ?)",
			uid, code, endTime);
		return code;
	}

	public void deleteLinkRequestByCode(String code) {
		jdbcTemplate.update("DELETE FROM public.\"LinkRequests\" WHERE code = ?", code);
	}

	public void deleteLinkRequestByUid(String uid) {
		jdbcTemplate.update("DELETE FROM public.\"LinkRequests\" WHERE uid = ?", uid);
	}

	public int tryToLinkCaretaker(String uid, String code) {
		List<LinkRequest> list = jdbcTemplate.query("SELECT * FROM public.\"LinkRequests\" WHERE code = ?",
			(resultSet, i) -> new LinkRequest(resultSet.getString("uid"), resultSet.getString("code"), resultSet.getLong("end_timestamp")), code);

		if (list.size() != 1) return 0;
		LinkRequest linkRequest = list.get(0);

		if (linkRequest.endTime > System.currentTimeMillis()) {
			jdbcTemplate.update("UPDATE public.\"CaredUsers\" SET caretaker_uid = ? WHERE uid = ?",
				uid, linkRequest.uid);
			return 1;
		}

		jdbcTemplate.update("DELETE FROM public.\"LinkRequests\" WHERE uid = ?", linkRequest.uid);
		return 0;
	}

	public boolean checkIfLinkCodeValid(String code) {
		List<Long> list = jdbcTemplate.query("SELECT * FROM public.\"LinkRequests\" WHERE code = ?",
			(resultSet, i) -> resultSet.getLong("end_timestamp"), code);

		if (list.size() == 0) return true;
		Long endTime = list.get(0);

		if (endTime > System.currentTimeMillis()) return false;
		else {
			deleteLinkRequestByCode(code);
			return true;
		}
	}

	// Remote

	public boolean checkRemote(String uid, String rUid) {
		List<CaredUser> list = jdbcTemplate.query("SELECT * FROM public.\"CaredUsers\" WHERE uid = ? AND caretaker_uid = ?", new CaredUser.Mapper(),
			rUid, uid);

		return list.size() != 0;
	}

	// Other

	public void addBugReport(String uid, String subject, String message, String info) {
		jdbcTemplate.update("INSERT INTO public.\"BugReports\" (uid, subject, message, info, time) VALUES (?, ?, ?, ?, ?)",
			uid, subject, message, info, System.currentTimeMillis());
	}
}
