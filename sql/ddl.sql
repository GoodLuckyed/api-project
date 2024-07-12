-- 创建数据库
create database if not exists api;

-- 切换数据库
use api;

-- 接口信息表
create table interface_info
(
    id             bigint auto_increment comment '主键'
        primary key,
    name           varchar(256)                           not null comment '接口名称',
    description    varchar(256)                           null comment '接口描述',
    url            varchar(512)                           not null comment '接口地址',
    requestParams  text                                   null comment '请求参数',
    responseParams text                                   null comment '响应参数',
    requestHeader  text                                   null comment '请求头',
    responseHeader text                                   null comment '响应头',
    returnFormat   varchar(512) default 'JSON'            null comment '返回格式',
    requestExample text                                   null comment '请求示例',
    reduceScore    int          default 0                 null comment '消耗积分数',
    avatarUrl      varchar(1024)                          null comment '接口头像',
    status         int          default 0                 not null comment '接口状态（0-关闭，1-开启）',
    method         varchar(256)                           not null comment '请求类型',
    userId         bigint                                 not null comment '创建人',
    totalInvokes   bigint       default 0                 null comment '接口总调用次数',
    createTime     datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime     datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete       tinyint      default 0                 not null comment '是否删除(0-未删, 1-已删)'
)
    comment '接口信息表';

-- 用户表
create table user
(
    id             bigint auto_increment comment 'id'
        primary key,
    userName       varchar(256)                           null comment '用户昵称',
    userAccount    varchar(256)                           not null comment '账号',
    avatarUrl      varchar(1024)                          null comment '用户头像',
    gender         tinyint                                null comment '性别',
    userRole       varchar(256) default 'user'            not null comment '用户角色：user / admin',
    userPassword   varchar(512)                           not null comment '密码',
    status         tinyint      default 0                 not null comment '用户状态 0-正常 1-封号',
    accessKey      varchar(512)                           not null comment 'accessKey',
    secretKey      varchar(512)                           not null comment 'secretKey',
    balance        int          default 0                 null comment '钱包余额（积分）',
    createTime     datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime     datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete       tinyint      default 0                 not null comment '是否删除',
    invitationCode varchar(256)                           null comment '邀请码',
    constraint uni_userAccount
        unique (userAccount)
)
    comment '用户';

-- 用户接口调用表
create table user_interface_invoke
(
    id           bigint auto_increment comment 'id' primary key,
    userId       bigint                             not null comment '调用人id',
    interfaceId  bigint                             not null comment '接口id',
    totalInvokes bigint   default 0                 not null comment '总调用次数',
    status       tinyint  default 0                 not null comment '调用状态（0- 正常 1- 封号）',
    createTime   datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint  default 0                 not null comment '是否删除'
)
    comment '用户接口调用表';

-- 产品信息表
create table api.product_info
(
    id             bigint auto_increment comment 'id'
        primary key,
    name           varchar(256)                           not null comment '产品名称',
    description    varchar(256)                           null comment '产品描述',
    userId         bigint                                 not null comment '创建人',
    total          int          default 0                 not null comment '金额（单位：分）',
    addPoints      int          default 0                 not null comment '增加积分个数',
    productType    varchar(256) default 'COIN'            not null comment '产品类型（VIP-会员  COIN-充值  COINACTIVITY-充值活动）',
    status         tinyint      default 0                 not null comment '产品状态（0-默认下线  1-上线）',
    expirationTime datetime                               null comment '过期时间',
    createTime     datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime     datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete       tinyint      default 0                 not null comment '是否删除'
)
    comment '产品信息表';

