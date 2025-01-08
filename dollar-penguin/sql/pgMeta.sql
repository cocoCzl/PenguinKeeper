---数据库信息表
DROP TABLE IF EXISTS database_information;
CREATE TABLE database_information (
    id SERIAL PRIMARY KEY,              -- 自增主键
    dataBaseCode int NOT NULL,          -- 数据库编码，不能为空
    dataBaseName VARCHAR(20) NOT NULL,  -- 数据库名称，不能为空
    url VARCHAR(128) NOT NULL,          -- 数据库连接串
    pwd VARCHAR(128) NOT NULL,          -- 数据库密码
    username VARCHAR(128) NOT NULL,     -- 数据库用户名
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,  -- 创建时间，默认为当前时间
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP   -- 修改时间，默认为当前时间
);

DROP TABLE IF EXISTS user_information;
CREATE TABLE user_information (
    id SERIAL PRIMARY KEY,              -- 自增主键
    user_id VARCHAR(64) NOT NULL,      -- 用户ID，不能为空
    user_name VARCHAR(16) NOT NULL,     -- 用户名，不能为空
    nick_name VARCHAR(16) NOT NULL,    -- 昵称，不能为空
    pwd VARCHAR(64) NOT NULL,          -- 密码，不能为空
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,  -- 创建时间，默认为当前时间
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP   -- 修改时间，默认为当前时间
);