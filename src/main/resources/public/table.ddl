-- table1
CREATE TABLE "task"."user" (
                               "id" int8 NOT NULL,
                               "name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                               "pwd" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                               "alia" varchar(64) COLLATE "pg_catalog"."default",
                               "avatar" varchar(255) COLLATE "pg_catalog"."default",
                               "is_online" int2,
                               CONSTRAINT "user_pkey" PRIMARY KEY ("id")
)
;

ALTER TABLE "task"."user"
    OWNER TO "postgres";

COMMENT ON COLUMN "task"."user"."alia" IS '别名';

COMMENT ON COLUMN "task"."user"."avatar" IS '头像';

COMMENT ON COLUMN "task"."user"."is_online" IS '0-不在，1-在';
-- table2
CREATE TABLE "task"."message" (
                                  "id" int8 NOT NULL,
                                  "send_time" timestamp(6),
                                  "context" varchar(512) COLLATE "pg_catalog"."default",
                                  "user1_id" int8,
                                  "user2_id" int8,
                                  CONSTRAINT "message_pkey" PRIMARY KEY ("id")
)
;

ALTER TABLE "task"."message"
    OWNER TO "postgres";

COMMENT ON COLUMN "task"."message"."user1_id" IS '发送人';

COMMENT ON COLUMN "task"."message"."user2_id" IS '接收人';
--table3
CREATE TABLE "task"."user_relation" (
                                        "id" int8 NOT NULL,
                                        "user1" int8,
                                        "user2" int8,
                                        "user1_name" varchar(64) COLLATE "pg_catalog"."default",
                                        "user2_name" varchar(64) COLLATE "pg_catalog"."default",
                                        "create_time" timestamp(6),
                                        CONSTRAINT "user_relation_pkey" PRIMARY KEY ("id")
)
;

ALTER TABLE "task"."user_relation"
    OWNER TO "postgres";

COMMENT ON COLUMN "task"."user_relation"."user1" IS '用户1_id';

COMMENT ON COLUMN "task"."user_relation"."user2" IS '用户2_id';

COMMENT ON COLUMN "task"."user_relation"."user1_name" IS '2对1的备注';

COMMENT ON COLUMN "task"."user_relation"."user2_name" IS '1对2的备注';

COMMENT ON COLUMN "task"."user_relation"."create_time" IS '关系创建时间';