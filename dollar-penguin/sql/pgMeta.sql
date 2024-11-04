---客户表
DROP TABLE IF EXISTS customer;
CREATE TABLE customer (
    id SERIAL PRIMARY KEY,               -- 自增主键
    c_code VARCHAR(20) UNIQUE NOT NULL,  -- 客户代码，唯一且不能为空
    c_name VARCHAR(50) NOT NULL,         -- 客户名称，不能为空
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,  -- 创建时间，默认为当前时间
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP   -- 修改时间，默认为当前时间
);

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