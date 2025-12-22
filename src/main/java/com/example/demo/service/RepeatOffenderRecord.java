package com.example.demo.service;

import com.example.demo.entity.RepeatOffenderRecord;

public interface RepeatOffenderRecordService {

    RepeatOffenderRecord saveRepeatOffenderRecord(RepeatOffenderRecord record);

    RepeatOffenderRecord getRepeatOffenderRecordById(Long id);
}
