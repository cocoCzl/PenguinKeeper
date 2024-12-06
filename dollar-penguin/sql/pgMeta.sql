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

---数据库表信息表
DROP TABLE IF EXISTS table_information;
CREATE TABLE table_information (
    id SERIAL PRIMARY KEY,               -- 自增主键
    dataBaseId int NOT NULL,             -- 数据库信息ID，不能为空
    schema_name VARCHAR(128) NOT NULL,   -- schema/catalog
    table_name VARCHAR(128) NOT NULL,    -- 表名
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,  -- 创建时间，默认为当前时间
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP   -- 修改时间，默认为当前时间
);

---列信息表
CREATE TABLE column_information (
    id SERIAL PRIMARY KEY,               -- 自增主键
    dataBaseId int NOT NULL,             -- 数据库信息ID，不能为空
    catalog_id int not null,             -- table表id
    table_name VARCHAR(128) NOT NULL,    -- 表名
    column_list VARCHAR(1024) NOT NULL,  -- 列名集合
    primary_key_list VARCHAR(128),       -- 主键集合
    foreign_key_list VARCHAR(128),       -- 外键集合
    index_list VARCHAR(256),             -- 索引集合
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,  -- 创建时间，默认为当前时间
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP   -- 修改时间，默认为当前时间
);