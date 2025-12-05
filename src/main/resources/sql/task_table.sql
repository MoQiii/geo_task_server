-- 创建任务表
CREATE TABLE IF NOT EXISTS task (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT DEFAULT '',
    due_date BIGINT NOT NULL,
    due_time BIGINT NOT NULL,
    is_completed BOOLEAN DEFAULT FALSE,
    is_reminder_enabled BOOLEAN DEFAULT FALSE,
    location VARCHAR(255),
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    geofence_radius REAL DEFAULT 200,
    created_at BIGINT DEFAULT EXTRACT(EPOCH FROM NOW()) * 1000,
    updated_at BIGINT DEFAULT EXTRACT(EPOCH FROM NOW()) * 1000
);

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_task_due_date ON task(due_date);
CREATE INDEX IF NOT EXISTS idx_task_is_completed ON task(is_completed);
CREATE INDEX IF NOT EXISTS idx_task_location ON task(latitude, longitude) WHERE latitude IS NOT NULL AND longitude IS NOT NULL;

-- 添加注释
COMMENT ON TABLE task IS '任务表';
COMMENT ON COLUMN task.id IS '主键ID';
COMMENT ON COLUMN task.title IS '任务标题';
COMMENT ON COLUMN task.description IS '任务描述';
COMMENT ON COLUMN task.due_date IS '截止日期（时间戳）';
COMMENT ON COLUMN task.due_time IS '截止时间（时间戳）';
COMMENT ON COLUMN task.is_completed IS '是否已完成';
COMMENT ON COLUMN task.is_reminder_enabled IS '是否启用提醒';
COMMENT ON COLUMN task.location IS '地址描述';
COMMENT ON COLUMN task.latitude IS '纬度';
COMMENT ON COLUMN task.longitude IS '经度';
COMMENT ON COLUMN task.geofence_radius IS '地理围栏半径（米）';
COMMENT ON COLUMN task.created_at IS '创建时间（时间戳）';
COMMENT ON COLUMN task.updated_at IS '更新时间（时间戳）';
