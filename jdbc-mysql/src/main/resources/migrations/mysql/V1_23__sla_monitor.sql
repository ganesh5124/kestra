CREATE TABLE IF NOT EXISTS sla_monitor (
    `key` VARCHAR(250) NOT NULL PRIMARY KEY,
    `value` JSONB NOT NULL,
    `execution_id` VARCHAR(150) NOT NULL GENERATED ALWAYS AS (value ->> '$.executionId') STORED,
    `sla_id` VARCHAR(150) NOT NULL GENERATED ALWAYS AS (value ->> '$.slaId') STORED,
    `deadline` DATETIME(6) NOT NULL GENERATED ALWAYS AS (STR_TO_DATE(value ->> '$.deadline' , '%Y-%m-%dT%H:%i:%s.%fZ')) STORED,
    INDEX ix_deadline (deadline),
    INDEX ix_execution_id (execution_id)
);