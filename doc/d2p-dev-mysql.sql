-- version 1.0

CREATE TABLE `d2p`.`users` (
                               `idx` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
                               `login_id` VARCHAR(100) NOT NULL COMMENT 'login ID' COLLATE 'utf8mb4_general_ci',
                               `login_pw` VARCHAR(100) NOT NULL COMMENT 'login Password' COLLATE 'utf8mb4_general_ci',
                               `nickname` VARCHAR(100) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
                               `user_status` ENUM('ACTIVE','INACTIVE') NOT NULL DEFAULT 'ACTIVE' COMMENT 'user 상태 INACTIVE => 비활성, ACTIVE => 활성' COLLATE 'utf8mb4_general_ci',
                               `user_grade` ENUM('ROLE_ADMIN','ROLE_USER','ROLE_INIT') NOT NULL DEFAULT 'ROLE_INIT' COMMENT 'user 등급 INIT => 초기, ADMIN => 관리자, USER => 일반' COLLATE 'utf8mb4_general_ci',
                               `created_at` DATETIME NOT NULL COMMENT '생성날짜',
                               `updated_at` DATETIME NULL DEFAULT NULL COMMENT '계정변경날짜',
                               `last_login_at` DATETIME NULL DEFAULT NULL COMMENT '마지막 로그인 날짜',
                               `password_error_count` TINYINT(1) NULL DEFAULT '0' COMMENT '비밀번호 오류 횟수',
                               PRIMARY KEY (`idx`) USING BTREE,
                               UNIQUE INDEX `users_login_id_uindex` (`login_id`) USING BTREE
)
    COMMENT='회원 table'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;

CREATE TABLE `d2p`.`user_password_logs` (
                                            `idx` BIGINT(20) UNSIGNED NOT NULL COMMENT 'Primary Key',
                                            `user_idx` BIGINT(20) NOT NULL COMMENT 'users table PK',
                                            `user_pw` VARCHAR(100) NOT NULL COMMENT '변경된 password' COLLATE 'utf8mb4_general_ci',
                                            `updated_at` DATETIME NOT NULL COMMENT '변경날짜'
)
    COMMENT='user 비밀번호 변경 log'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;



CREATE TABLE `d2p`.`sandboxes` (
                                   `idx` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
                                   `name` VARCHAR (50) NOT NULL COMMENT '샌드박스 이름' COLLATE 'utf8mb4_general_ci',
                                   `sandbox_model_idx` INT(11) UNSIGNED NOT NULL COMMENT 'sandbox_models Table PK',
                                   `volume` INT(4) NOT NULL COMMENT '용량',
                                   `cloud_instance_uuid` VARCHAR(100) COMMENT 'cloud_instance Table UUID' COLLATE 'utf8mb4_general_ci',
                                   `status` ENUM('WAITING','REJECTED','ACTIVE','PROGRESS','TERMINATED') NOT NULL DEFAULT 'WAITING' COMMENT '상태' COLLATE 'utf8mb4_general_ci',
                                   `description` TEXT NULL DEFAULT NULL COMMENT '설명' COLLATE 'utf8mb4_general_ci',
                                   `created_at` DATETIME NULL DEFAULT NOT NULL COMMENT '생성날짜',
                                   `created_user_idx` BIGINT(20) UNSIGNED NOT NULL COMMENT '생성 user PK',
                                   `updated_at` DATETIME NULL DEFAULT NULL COMMENT '수정날짜',
                                   `updated_user_idx` BIGINT(20) UNSIGNED NULL COMMENT '수정 user PK',
                                   `auto_stop_yn` VARCHAR(1) NOT NULL DEFAULT 0 COMMENT '자동중지 여부',
                                   PRIMARY KEY (`idx`) USING BTREE
)
COMMENT='샌드박스'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;


CREATE TABLE `d2p`.`sandbox_spec` (
                                      `sandbox_spec_idx` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
                                      `sandbox_type` VARCHAR(20) NOT NULL COMMENT '샌드박스 종류 ex) EC2, AppStream' COLLATE 'utf8mb4_general_ci',
                                      `sandbox_os` VARCHAR(20) NOT NULL COMMENT 'OS' COLLATE 'utf8mb4_general_ci',
                                      `instance_type` VARCHAR(20) NOT NULL COMMENT '인스턴스 타입 ex) t2.small, m4.large' COLLATE 'utf8mb4_general_ci',
                                      `sandbox_cpu` INT(4) NOT NULL DEFAULT '0' COMMENT 'cpu',
                                      `sandbox_memory` INT(4) NOT NULL DEFAULT '0' COMMENT 'memory',
                                      `sandbox_network_description` VARCHAR(50) NULL DEFAULT NULL COMMENT '네트워크 설명' COLLATE 'utf8mb4_general_ci',
                                      `sandbox_price` FLOAT NULL DEFAULT NULL COMMENT '금액',
                                      `sandbox_spec_status` ENUM('ACTIVE','INACTIVE') NULL DEFAULT 'ACTIVE' COMMENT '상태' COLLATE 'utf8mb4_general_ci',
                                      `sandbox_spec_created_at` DATETIME NULL DEFAULT NULL COMMENT '생성날짜',
                                      `sandbox_spec_updated_at` DATETIME NULL DEFAULT NULL COMMENT '수정날짜',
                                      PRIMARY KEY (`sandbox_spec_idx`) USING BTREE
)
    COMMENT='샌드박스 스펙'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;


CREATE TABLE `d2p`.`sandbox_images` (
                                        `sandbox_image_idx` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
                                        `sandbox_type` VARCHAR(20) NOT NULL COMMENT 'sandbox type' COLLATE 'utf8mb4_general_ci',
                                        `sandbox_os` VARCHAR(20) NOT NULL COMMENT 'OS' COLLATE 'utf8mb4_general_ci',
                                        `cloud_image_id` VARCHAR(50) NOT NULL COMMENT 'Cloud Ami ID' COLLATE 'utf8mb4_general_ci',
                                        `sandbox_image_description` TEXT NOT NULL COMMENT '설명' COLLATE 'utf8mb4_general_ci',
                                        `sandbox_image_tags` JSON NULL DEFAULT NULL COMMENT '태그',
                                        `sandbox_image_status` ENUM('ACTIVE','INACTIVE') NULL DEFAULT 'ACTIVE' COMMENT '상태' COLLATE 'utf8mb4_general_ci',
                                        `sandbox_image_volume` INT(4) NULL DEFAULT '0' COMMENT '용량',
                                        `sandbox_image_created_user_idx` INT(11) NOT NULL COMMENT '생성 User PK',
                                        `sandbox_image_created_at` DATETIME NOT NULL COMMENT '생성일자',
                                        `sandbox_image_updated_user_idx` INT(11) NULL DEFAULT '0' COMMENT '수정 User PK',
                                        `sandbox_image_updated_at` DATETIME NULL DEFAULT NULL COMMENT '수정일자',
                                        PRIMARY KEY (`sandbox_image_idx`) USING BTREE,
                                        UNIQUE INDEX `cloud_image_id` (`cloud_image_id`) USING BTREE
)
    COMMENT='샌드박스 이미지'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;

CREATE TABLE `d2p`.`sandbox_models` (
                                        `sandbox_model_idx` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
                                        `sandbox_model_name` VARCHAR(30) NOT NULL COMMENT '모델 명' COLLATE 'utf8mb4_general_ci',
                                        `sandbox_spec_idx` INT(11) NULL DEFAULT '0' COMMENT 'sandbox_spec PK',
                                        `sandbox_image_idx` INT(11) NULL DEFAULT '0' COMMENT 'sandbox_image PK',
                                        `sandbox_model_description` TEXT NOT NULL COMMENT '모델 설명' COLLATE 'utf8mb4_general_ci',
                                        `sandbox_model_tags` JSON NOT NULL COMMENT '태그',
                                        `sandbox_model_status` ENUM('ACTIVE','INACTIVE') NULL DEFAULT 'ACTIVE' COMMENT '상태' COLLATE 'utf8mb4_general_ci',
                                        `sandbox_model_created_user_idx` INT(11) NOT NULL COMMENT '생성 user PK',
                                        `sandbox_model_created_at` DATETIME NOT NULL COMMENT '생성날짜',
                                        `sandbox_model_updated_user_idx` INT(11) NULL DEFAULT '0' COMMENT '수정 user PK',
                                        `sandbox_model_updated_at` DATETIME NULL DEFAULT NULL COMMENT '수정날짜',
                                        `sandbox_model_type` ENUM('EC2','SAGEMAKER') NULL DEFAULT 'EC2' COMMENT '타입' COLLATE 'utf8mb4_general_ci',
                                        PRIMARY KEY (`sandbox_model_idx`) USING BTREE,
                                        UNIQUE INDEX `sandbox_model_name` (`sandbox_model_name`) USING BTREE
)
    COMMENT='샌드박스 모델'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;

CREATE TABLE `master_resources` (
                                    `idx` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
                                    `type` VARCHAR(20) NOT NULL COMMENT '리소스 타입' COLLATE 'utf8mb4_general_ci',
                                    `name` VARCHAR(100) NOT NULL COMMENT '리소스 이름' COLLATE 'utf8mb4_general_ci',
                                    `status` VARCHAR(20) NOT NULL COMMENT '리소스 상태' COLLATE 'utf8mb4_general_ci',
                                    `created_at` DATETIME NOT NULL COMMENT '리소스 등록날짜',
                                    `updated_at` DATETIME NULL DEFAULT NULL COMMENT '리소스 수정날짜',
                                    `group` VARCHAR(20) NULL DEFAULT NULL COMMENT '리소스 그룹' COLLATE 'utf8mb4_general_ci',
                                    PRIMARY KEY (`idx`) USING BTREE
)
    COMMENT='마스터 리소스'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;


CREATE TABLE `cloud_sandbox_instances` (
                                           `idx` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
                                           `instance_uuid` VARCHAR(100) NULL DEFAULT NULL COMMENT 'Uuid' COLLATE 'utf8mb4_general_ci',
                                           `instance_type` ENUM('EC2', 'SAGEMAKER') NOT NULL DEFAULT 'EC2',
                                           `instance_id` VARCHAR(100) NOT NULL COMMENT 'Instance ID' COLLATE 'utf8mb4_general_ci',
                                           `ami_id` VARCHAR(100) NULL COMMENT 'Ami ID' COLLATE 'utf8mb4_general_ci',
                                           `private_ip` VARCHAR(50) NULL DEFAULT NULL COMMENT 'Private IP' COLLATE 'utf8mb4_general_ci',
                                           `connection_private_url` VARCHAR(100) NULL DEFAULT NULL COMMENT 'Private IP' COLLATE 'utf8mb4_general_ci',
                                           `connection_public_url`  VARCHAR(100) NULL DEFAULT NULL COMMENT 'Public IP' COLLATE 'utf8mb4_general_ci',
                                           `volume` INT(8) NULL DEFAULT NULL COMMENT '용량',
                                           `status` VARCHAR(20) NULL DEFAULT NULL COMMENT '상태' COLLATE 'utf8mb4_general_ci',
                                           `cloud_tags` LONGTEXT NULL DEFAULT NULL COMMENT '태그' COLLATE 'utf8mb4_bin',
                                           `started_at` DATETIME NULL DEFAULT NULL COMMENT '시작날짜',
                                           `stopped_at` DATETIME NULL DEFAULT NULL COMMENT '중지날짜',
                                           `terminated_at` DATETIME NULL DEFAULT NULL COMMENT '종료날짜',
                                           `created_at` DATETIME NOT NULL COMMENT '생성날짜',
                                           `updated_at` DATETIME NULL DEFAULT NULL COMMENT '수정날짜',
                                           PRIMARY KEY (`idx`) USING BTREE,
                                           CONSTRAINT `cloud_tags` CHECK (json_valid(`cloud_tags`))
)
    COMMENT='Cloud 샌드박스 인스턴스'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;


TRUNCATE `d2p`.`sandbox_spec`;

INSERT INTO `d2p`.`sandbox_spec`
(sandbox_spec_idx, sandbox_type, sandbox_os, instance_type, sandbox_cpu, sandbox_memory, sandbox_network_description, sandbox_price, sandbox_spec_status, sandbox_spec_created_at, sandbox_spec_updated_at)
VALUES
(1, 'EC2', 'LINUX', 'c4.2xlarge', 8, 15, 'High', 0.454, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(2, 'EC2', 'WINDOWS', 'c4.2xlarge', 8, 15, 'High', 0.822, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(3, 'EC2', 'LINUX', 'c4.4xlarge', 16, 30, 'High', 0.907, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(4, 'EC2', 'WINDOWS', 'c4.4xlarge', 16, 30, 'High', 1.643, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(5, 'EC2', 'LINUX', 'c4.8xlarge', 36, 60, '10 Gigabit', 1.815, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(6, 'EC2', 'WINDOWS', 'c4.8xlarge', 36, 60, '10 Gigabit', 3.471, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(7, 'EC2', 'WINDOWS', 'c4.large', 2, 3, 'Moderate', 0.206, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(8, 'EC2', 'LINUX', 'c4.large', 2, 3, 'Moderate', 0.114, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(9, 'EC2', 'LINUX', 'c4.xlarge', 4, 7, 'High', 0.227, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(10, 'SAGEMAKER', 'WINDOWS', 'c4.xlarge', 4, 7, 'High', 0.411, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(11, 'SAGEMAKER', 'WINDOWS', 'c5.12xlarge', 48, 96, '12 Gigabit', 4.512, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(12, 'SAGEMAKER', 'LINUX', 'c5.12xlarge', 48, 96, '12 Gigabit', 2.304, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(13, 'SAGEMAKER', 'WINDOWS', 'c5.18xlarge', 72, 144, '25 Gigabit', 6.768, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(14, 'SAGEMAKER', 'LINUX', 'c5.18xlarge', 72, 144, '25 Gigabit', 3.456, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(15, 'SAGEMAKER', 'LINUX', 'c5.24xlarge', 96, 192, '25 Gigabit', 4.608, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(16, 'EC2', 'WINDOWS', 'c5.24xlarge', 96, 192, '25 Gigabit', 9.024, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(17, 'EC2', 'WINDOWS', 'c5.2xlarge', 8, 16, 'Up to 10 Gigabit', 0.752, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(18, 'EC2', 'LINUX', 'c5.2xlarge', 8, 16, 'Up to 10 Gigabit', 0.384, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(19, 'EC2', 'LINUX', 'c5.4xlarge', 16, 32, 'Up to 10 Gigabit', 0.768, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(20, 'EC2', 'WINDOWS', 'c5.4xlarge', 16, 32, 'Up to 10 Gigabit', 1.504, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(21, 'EC2', 'LINUX', 'c5.9xlarge', 36, 72, '10 Gigabit', 1.728, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(22, 'EC2', 'WINDOWS', 'c5.9xlarge', 36, 72, '10 Gigabit', 3.384, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(23, 'EC2', 'LINUX', 'c5.large', 2, 4, 'Up to 10 Gigabit', 0.096, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(24, 'EC2', 'WINDOWS', 'c5.large', 2, 4, 'Up to 10 Gigabit', 0.188, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(25, 'EC2', 'WINDOWS', 'c5.metal', 96, 192, '25 Gigabit', 9.024, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(26, 'EC2', 'LINUX', 'c5.metal', 96, 192, '25 Gigabit', 4.608, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(27, 'EC2', 'LINUX', 'c5.xlarge', 4, 8, 'Up to 10 Gigabit', 0.192, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(28, 'EC2', 'WINDOWS', 'c5.xlarge', 4, 8, 'Up to 10 Gigabit', 0.376, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(29, 'EC2', 'LINUX', 'c5d.12xlarge', 48, 96, '12 Gigabit', 2.64, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(30, 'EC2', 'WINDOWS', 'c5d.12xlarge', 48, 96, '12 Gigabit', 4.848, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(31, 'EC2', 'WINDOWS', 'c5d.18xlarge', 72, 144, '25 Gigabit', 7.272, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(32, 'EC2', 'LINUX', 'c5d.18xlarge', 72, 144, '25 Gigabit', 3.96, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(33, 'EC2', 'WINDOWS', 'c5d.24xlarge', 96, 192, '25 Gigabit', 9.696, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(34, 'EC2', 'LINUX', 'c5d.24xlarge', 96, 192, '25 Gigabit', 5.28, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(35, 'EC2', 'WINDOWS', 'c5d.2xlarge', 8, 16, 'Up to 10 Gigabit', 0.808, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(36, 'EC2', 'LINUX', 'c5d.2xlarge', 8, 16, 'Up to 10 Gigabit', 0.44, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(37, 'EC2', 'LINUX', 'c5d.4xlarge', 16, 32, 'Up to 10 Gigabit', 0.88, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(38, 'EC2', 'WINDOWS', 'c5d.4xlarge', 16, 32, 'Up to 10 Gigabit', 1.616, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(39, 'EC2', 'WINDOWS', 'c5d.9xlarge', 36, 72, '10 Gigabit', 3.636, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(40, 'EC2', 'LINUX', 'c5d.9xlarge', 36, 72, '10 Gigabit', 1.98, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(41, 'EC2', 'WINDOWS', 'c5d.large', 2, 4, 'Up to 10 Gigabit', 0.202, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(42, 'EC2', 'LINUX', 'c5d.large', 2, 4, 'Up to 10 Gigabit', 0.11, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(43, 'EC2', 'WINDOWS', 'c5d.metal', 96, 192, '25 Gigabit', 9.696, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(44, 'EC2', 'LINUX', 'c5d.metal', 96, 192, '25 Gigabit', 5.28, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(45, 'EC2', 'LINUX', 'c5d.xlarge', 4, 8, 'Up to 10 Gigabit', 0.22, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(46, 'EC2', 'WINDOWS', 'c5d.xlarge', 4, 8, 'Up to 10 Gigabit', 0.404, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(47, 'EC2', 'WINDOWS', 'c5n.18xlarge', 72, 192, '100 Gigabit', 7.704, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(48, 'EC2', 'LINUX', 'c5n.18xlarge', 72, 192, '100 Gigabit', 4.392, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(49, 'EC2', 'LINUX', 'c5n.2xlarge', 8, 21, 'Up to 25 Gigabit', 0.488, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(50, 'EC2', 'WINDOWS', 'c5n.2xlarge', 8, 21, 'Up to 25 Gigabit', 0.856, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(51, 'EC2', 'WINDOWS', 'c5n.4xlarge', 16, 42, 'Up to 25 Gigabit', 1.712, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(52, 'EC2', 'LINUX', 'c5n.4xlarge', 16, 42, 'Up to 25 Gigabit', 0.976, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(53, 'EC2', 'LINUX', 'c5n.9xlarge', 36, 96, '50 Gigabit', 2.196, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(54, 'EC2', 'WINDOWS', 'c5n.9xlarge', 36, 96, '50 Gigabit', 3.852, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(55, 'EC2', 'LINUX', 'c5n.large', 2, 5, 'Up to 25 Gigabit', 0.122, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(56, 'EC2', 'WINDOWS', 'c5n.large', 2, 5, 'Up to 25 Gigabit', 0.214, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(57, 'EC2', 'WINDOWS', 'c5n.metal', 72, 192, '100 Gigabit', 7.704, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(58, 'EC2', 'LINUX', 'c5n.metal', 72, 192, '100 Gigabit', 4.392, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(59, 'EC2', 'LINUX', 'c5n.xlarge', 4, 10, 'Up to 25 Gigabit', 0.244, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(60, 'EC2', 'WINDOWS', 'c5n.xlarge', 4, 10, 'Up to 25 Gigabit', 0.428, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(61, 'EC2', 'WINDOWS', 'd2.2xlarge', 8, 61, 'High', 2.056, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(62, 'EC2', 'LINUX', 'd2.2xlarge', 8, 61, 'High', 1.688, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(63, 'EC2', 'LINUX', 'd2.4xlarge', 16, 122, 'High', 3.376, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(64, 'EC2', 'WINDOWS', 'd2.4xlarge', 16, 122, 'High', 4.112, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(65, 'EC2', 'LINUX', 'd2.8xlarge', 36, 244, '10 Gigabit', 6.752, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(66, 'EC2', 'WINDOWS', 'd2.8xlarge', 36, 244, '10 Gigabit', 8.408, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(67, 'EC2', 'LINUX', 'd2.xlarge', 4, 30, 'Moderate', 0.844, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(68, 'EC2', 'WINDOWS', 'd2.xlarge', 4, 30, 'Moderate', 1.028, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(69, 'EC2', 'WINDOWS', 'g3.16xlarge', 64, 488, '20 Gigabit', 8.624, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(70, 'EC2', 'LINUX', 'g3.16xlarge', 64, 488, '20 Gigabit', 5.68, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(71, 'EC2', 'LINUX', 'g3.4xlarge', 16, 122, 'Up to 10 Gigabit', 1.42, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(72, 'EC2', 'WINDOWS', 'g3.4xlarge', 16, 122, 'Up to 10 Gigabit', 2.156, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(73, 'EC2', 'WINDOWS', 'g3.8xlarge', 32, 244, '10 Gigabit', 4.312, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(74, 'EC2', 'LINUX', 'g3.8xlarge', 32, 244, '10 Gigabit', 2.84, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(75, 'EC2', 'WINDOWS', 'g3s.xlarge', 4, 30, '10 Gigabit', 1.118, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(76, 'EC2', 'LINUX', 'g3s.xlarge', 4, 30, '10 Gigabit', 0.934, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(77, 'EC2', 'WINDOWS', 'g4dn.12xlarge', 48, 192, '50 Gigabit', 7.02, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(78, 'EC2', 'LINUX', 'g4dn.12xlarge', 48, 192, '50 Gigabit', 4.812, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(79, 'EC2', 'WINDOWS', 'g4dn.16xlarge', 64, 256, '50 Gigabit', 8.297, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(80, 'EC2', 'LINUX', 'g4dn.16xlarge', 64, 256, '50 Gigabit', 5.353, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(81, 'EC2', 'LINUX', 'g4dn.2xlarge', 8, 32, 'Up to 25 Gigabit', 0.925, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(82, 'EC2', 'WINDOWS', 'g4dn.2xlarge', 8, 32, 'Up to 25 Gigabit', 1.293, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(83, 'EC2', 'WINDOWS', 'g4dn.4xlarge', 16, 64, 'Up to 25 Gigabit', 2.217, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(84, 'EC2', 'LINUX', 'g4dn.4xlarge', 16, 64, 'Up to 25 Gigabit', 1.481, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(85, 'EC2', 'LINUX', 'g4dn.8xlarge', 32, 128, '50 Gigabit', 2.677, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(86, 'EC2', 'WINDOWS', 'g4dn.8xlarge', 32, 128, '50 Gigabit', 4.149, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(87, 'EC2', 'LINUX', 'g4dn.xlarge', 4, 16, 'Up to 25 Gigabit', 0.647, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(88, 'EC2', 'WINDOWS', 'g4dn.xlarge', 4, 16, 'Up to 25 Gigabit', 0.831, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(89, 'EC2', 'LINUX', 'i2.2xlarge', 8, 61, 'High', 2.001, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(90, 'EC2', 'WINDOWS', 'i2.2xlarge', 8, 61, 'High', 2.369, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(91, 'EC2', 'WINDOWS', 'i2.4xlarge', 16, 122, 'High', 4.738, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(92, 'EC2', 'LINUX', 'i2.4xlarge', 16, 122, 'High', 4.002, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(93, 'EC2', 'WINDOWS', 'i2.8xlarge', 32, 244, '10 Gigabit', 9.476, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(94, 'EC2', 'LINUX', 'i2.8xlarge', 32, 244, '10 Gigabit', 8.004, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(95, 'EC2', 'WINDOWS', 'i2.xlarge', 4, 30, 'Moderate', 1.185, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(96, 'EC2', 'LINUX', 'i2.xlarge', 4, 30, 'Moderate', 1.001, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(97, 'EC2', 'LINUX', 'i3.16xlarge', 64, 488, '20 Gigabit', 5.856, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(98, 'EC2', 'WINDOWS', 'i3.16xlarge', 64, 488, '20 Gigabit', 8.8, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(99, 'EC2', 'WINDOWS', 'i3.2xlarge', 8, 61, 'Up to 10 Gigabit', 1.1, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(100, 'EC2', 'LINUX', 'i3.2xlarge', 8, 61, 'Up to 10 Gigabit', 0.732, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(101, 'EC2', 'LINUX', 'i3.4xlarge', 16, 122, 'Up to 10 Gigabit', 1.464, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(102, 'EC2', 'WINDOWS', 'i3.4xlarge', 16, 122, 'Up to 10 Gigabit', 2.2, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(103, 'EC2', 'LINUX', 'i3.8xlarge', 32, 244, '10 Gigabit', 2.928, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(104, 'EC2', 'WINDOWS', 'i3.8xlarge', 32, 244, '10 Gigabit', 4.4, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(105, 'EC2', 'LINUX', 'i3.large', 2, 15, 'Up to 10 Gigabit', 0.183, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(106, 'EC2', 'WINDOWS', 'i3.large', 2, 15, 'Up to 10 Gigabit', 0.275, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(107, 'EC2', 'LINUX', 'i3.xlarge', 4, 30, 'Up to 10 Gigabit', 0.366, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(108, 'EC2', 'WINDOWS', 'i3.xlarge', 4, 30, 'Up to 10 Gigabit', 0.55, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(109, 'EC2', 'WINDOWS', 'i3en.12xlarge', 48, 384, '50 Gigabit', 8.592, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(110, 'EC2', 'LINUX', 'i3en.12xlarge', 48, 384, '50 Gigabit', 6.384, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(111, 'EC2', 'WINDOWS', 'i3en.24xlarge', 96, 768, '100 Gigabit', 17.184, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(112, 'EC2', 'LINUX', 'i3en.24xlarge', 96, 768, '100 Gigabit', 12.768, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(113, 'EC2', 'LINUX', 'i3en.2xlarge', 8, 64, 'Up to 25 Gigabit', 1.064, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(114, 'EC2', 'WINDOWS', 'i3en.2xlarge', 8, 64, 'Up to 25 Gigabit', 1.432, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(115, 'EC2', 'WINDOWS', 'i3en.3xlarge', 12, 96, 'Up to 25 Gigabit', 2.148, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(116, 'EC2', 'LINUX', 'i3en.3xlarge', 12, 96, 'Up to 25 Gigabit', 1.596, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(117, 'EC2', 'WINDOWS', 'i3en.6xlarge', 24, 192, '25 Gigabit', 4.296, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(118, 'EC2', 'LINUX', 'i3en.6xlarge', 24, 192, '25 Gigabit', 3.192, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(119, 'EC2', 'WINDOWS', 'i3en.large', 2, 16, 'Up to 25 Gigabit', 0.358, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(120, 'EC2', 'LINUX', 'i3en.large', 2, 16, 'Up to 25 Gigabit', 0.266, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(121, 'EC2', 'LINUX', 'i3en.metal', 96, 768, '100 Gigabit', 12.768, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(122, 'EC2', 'WINDOWS', 'i3en.metal', 96, 768, '100 Gigabit', 17.184, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(123, 'EC2', 'LINUX', 'i3en.xlarge', 4, 32, 'Up to 25 Gigabit', 0.532, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(124, 'EC2', 'WINDOWS', 'i3en.xlarge', 4, 32, 'Up to 25 Gigabit', 0.716, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(125, 'EC2', 'WINDOWS', 'm4.10xlarge', 40, 160, '10 Gigabit', 4.3, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(126, 'EC2', 'LINUX', 'm4.10xlarge', 40, 160, '10 Gigabit', 2.46, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(127, 'EC2', 'LINUX', 'm4.16xlarge', 64, 256, '20 Gigabit', 3.936, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(128, 'EC2', 'WINDOWS', 'm4.16xlarge', 64, 256, '20 Gigabit', 6.88, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(129, 'EC2', 'WINDOWS', 'm4.2xlarge', 8, 32, 'High', 0.86, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(130, 'EC2', 'LINUX', 'm4.2xlarge', 8, 32, 'High', 0.492, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(131, 'EC2', 'WINDOWS', 'm4.4xlarge', 16, 64, 'High', 1.72, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(132, 'EC2', 'LINUX', 'm4.4xlarge', 16, 64, 'High', 0.984, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(133, 'EC2', 'WINDOWS', 'm4.large', 2, 8, 'Moderate', 0.215, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(134, 'EC2', 'LINUX', 'm4.large', 2, 8, 'Moderate', 0.123, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(135, 'EC2', 'WINDOWS', 'm4.xlarge', 4, 16, 'High', 0.43, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(136, 'EC2', 'LINUX', 'm4.xlarge', 4, 16, 'High', 0.246, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(137, 'EC2', 'LINUX', 'm5.12xlarge', 48, 192, '10 Gigabit', 2.832, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(138, 'EC2', 'WINDOWS', 'm5.12xlarge', 48, 192, '10 Gigabit', 5.04, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(139, 'EC2', 'WINDOWS', 'm5.16xlarge', 64, 256, '20 Gigabit', 6.72, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(140, 'EC2', 'LINUX', 'm5.16xlarge', 64, 256, '20 Gigabit', 3.776, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(141, 'EC2', 'WINDOWS', 'm5.24xlarge', 96, 384, '25 Gigabit', 10.08, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(142, 'EC2', 'LINUX', 'm5.24xlarge', 96, 384, '25 Gigabit', 5.664, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(143, 'EC2', 'LINUX', 'm5.2xlarge', 8, 32, 'Up to 10 Gigabit', 0.472, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(144, 'EC2', 'WINDOWS', 'm5.2xlarge', 8, 32, 'Up to 10 Gigabit', 0.84, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(145, 'EC2', 'WINDOWS', 'm5.4xlarge', 16, 64, 'Up to 10 Gigabit', 1.68, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(146, 'EC2', 'LINUX', 'm5.4xlarge', 16, 64, 'Up to 10 Gigabit', 0.944, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(147, 'EC2', 'WINDOWS', 'm5.8xlarge', 32, 128, '10 Gigabit', 3.36, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(148, 'EC2', 'LINUX', 'm5.8xlarge', 32, 128, '10 Gigabit', 1.888, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(149, 'EC2', 'LINUX', 'm5.large', 2, 8, 'Up to 10 Gigabit', 0.118, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(150, 'EC2', 'WINDOWS', 'm5.large', 2, 8, 'Up to 10 Gigabit', 0.21, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(151, 'EC2', 'LINUX', 'm5.metal', 96, 384, '25 Gigabit', 5.664, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(152, 'EC2', 'WINDOWS', 'm5.metal', 96, 384, '25 Gigabit', 10.08, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(153, 'EC2', 'WINDOWS', 'm5.xlarge', 4, 16, 'Up to 10 Gigabit', 0.42, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(154, 'EC2', 'LINUX', 'm5.xlarge', 4, 16, 'Up to 10 Gigabit', 0.236, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(155, 'EC2', 'LINUX', 'm5a.12xlarge', 48, 192, '10 Gigabit', 2.544, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(156, 'EC2', 'WINDOWS', 'm5a.12xlarge', 48, 192, '10 Gigabit', 4.752, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(157, 'EC2', 'WINDOWS', 'm5a.16xlarge', 64, 256, '12 Gigabit', 6.336, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(158, 'EC2', 'LINUX', 'm5a.16xlarge', 64, 256, '12 Gigabit', 3.392, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(159, 'EC2', 'LINUX', 'm5a.24xlarge', 96, 384, '20 Gigabit', 5.088, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(160, 'EC2', 'WINDOWS', 'm5a.24xlarge', 96, 384, '20 Gigabit', 9.504, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(161, 'EC2', 'LINUX', 'm5a.2xlarge', 8, 32, 'Up to 10 Gigabit', 0.424, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(162, 'EC2', 'WINDOWS', 'm5a.2xlarge', 8, 32, 'Up to 10 Gigabit', 0.792, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(163, 'EC2', 'LINUX', 'm5a.4xlarge', 16, 64, 'Up to 10 Gigabit', 0.848, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(164, 'EC2', 'WINDOWS', 'm5a.4xlarge', 16, 64, 'Up to 10 Gigabit', 1.584, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(165, 'EC2', 'LINUX', 'm5a.8xlarge', 32, 128, 'Up to 10 Gigabit', 1.696, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(166, 'EC2', 'WINDOWS', 'm5a.8xlarge', 32, 128, 'Up to 10 Gigabit', 3.168, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(167, 'EC2', 'LINUX', 'm5a.large', 2, 8, 'Up to 10 Gigabit', 0.106, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(168, 'EC2', 'WINDOWS', 'm5a.large', 2, 8, 'Up to 10 Gigabit', 0.198, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(169, 'EC2', 'WINDOWS', 'm5a.xlarge', 4, 16, 'Up to 10 Gigabit', 0.396, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(170, 'EC2', 'LINUX', 'm5a.xlarge', 4, 16, 'Up to 10 Gigabit', 0.212, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(171, 'EC2', 'WINDOWS', 'm5ad.12xlarge', 48, 192, '10 Gigabit', 5.256, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(172, 'EC2', 'LINUX', 'm5ad.12xlarge', 48, 192, '10 Gigabit', 3.048, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(173, 'EC2', 'LINUX', 'm5ad.16xlarge', 64, 256, '12 Gigabit', 4.064, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(174, 'EC2', 'WINDOWS', 'm5ad.16xlarge', 64, 256, '12 Gigabit', 7.008, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(175, 'EC2', 'WINDOWS', 'm5ad.24xlarge', 96, 384, '20 Gigabit', 10.512, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(176, 'EC2', 'LINUX', 'm5ad.24xlarge', 96, 384, '20 Gigabit', 6.096, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(177, 'EC2', 'LINUX', 'm5ad.2xlarge', 8, 32, 'Up to 10 Gigabit', 0.508, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(178, 'EC2', 'WINDOWS', 'm5ad.2xlarge', 8, 32, 'Up to 10 Gigabit', 0.876, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(179, 'EC2', 'LINUX', 'm5ad.4xlarge', 16, 64, 'Up to 10 Gigabit', 1.016, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(180, 'EC2', 'WINDOWS', 'm5ad.4xlarge', 16, 64, 'Up to 10 Gigabit', 1.752, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(181, 'EC2', 'WINDOWS', 'm5ad.8xlarge', 32, 128, 'Up to 10 Gigabit', 3.504, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(182, 'EC2', 'LINUX', 'm5ad.8xlarge', 32, 128, 'Up to 10 Gigabit', 2.032, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(183, 'EC2', 'WINDOWS', 'm5ad.large', 2, 8, 'Up to 10 Gigabit', 0.219, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(184, 'EC2', 'LINUX', 'm5ad.large', 2, 8, 'Up to 10 Gigabit', 0.127, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(185, 'EC2', 'WINDOWS', 'm5ad.xlarge', 4, 16, 'Up to 10 Gigabit', 0.438, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(186, 'EC2', 'LINUX', 'm5ad.xlarge', 4, 16, 'Up to 10 Gigabit', 0.254, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(187, 'EC2', 'LINUX', 'm5d.12xlarge', 48, 192, '10 Gigabit', 3.336, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(188, 'EC2', 'WINDOWS', 'm5d.12xlarge', 48, 192, '10 Gigabit', 5.544, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(189, 'EC2', 'WINDOWS', 'm5d.16xlarge', 64, 256, '20 Gigabit', 7.392, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(190, 'EC2', 'LINUX', 'm5d.16xlarge', 64, 256, '20 Gigabit', 4.448, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(191, 'EC2', 'LINUX', 'm5d.24xlarge', 96, 384, '25 Gigabit', 6.672, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(192, 'EC2', 'WINDOWS', 'm5d.24xlarge', 96, 384, '25 Gigabit', 11.088, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(193, 'EC2', 'WINDOWS', 'm5d.2xlarge', 8, 32, 'Up to 10 Gigabit', 0.924, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(194, 'EC2', 'LINUX', 'm5d.2xlarge', 8, 32, 'Up to 10 Gigabit', 0.556, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(195, 'EC2', 'WINDOWS', 'm5d.4xlarge', 16, 64, 'Up to 10 Gigabit', 1.848, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(196, 'EC2', 'LINUX', 'm5d.4xlarge', 16, 64, 'Up to 10 Gigabit', 1.112, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(197, 'EC2', 'WINDOWS', 'm5d.8xlarge', 32, 128, '10 Gigabit', 3.696, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(198, 'EC2', 'LINUX', 'm5d.8xlarge', 32, 128, '10 Gigabit', 2.224, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(199, 'EC2', 'LINUX', 'm5d.large', 2, 8, 'Up to 10 Gigabit', 0.139, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(200, 'EC2', 'WINDOWS', 'm5d.large', 2, 8, 'Up to 10 Gigabit', 0.231, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(201, 'EC2', 'LINUX', 'm5d.metal', 96, 384, '25 Gigabit', 6.672, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(202, 'EC2', 'WINDOWS', 'm5d.metal', 96, 384, '25 Gigabit', 11.088, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(203, 'EC2', 'WINDOWS', 'm5d.xlarge', 4, 16, 'Up to 10 Gigabit', 0.462, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(204, 'EC2', 'LINUX', 'm5d.xlarge', 4, 16, 'Up to 10 Gigabit', 0.278, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(205, 'EC2', 'WINDOWS', 'p2.16xlarge', 64, 732, '20 Gigabit', 26.384, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(206, 'EC2', 'LINUX', 'p2.16xlarge', 64, 732, '20 Gigabit', 23.44, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(207, 'EC2', 'WINDOWS', 'p2.8xlarge', 32, 488, '10 Gigabit', 13.192, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(208, 'EC2', 'LINUX', 'p2.8xlarge', 32, 488, '10 Gigabit', 11.72, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(209, 'EC2', 'LINUX', 'p2.xlarge', 4, 61, 'High', 1.465, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(210, 'EC2', 'WINDOWS', 'p2.xlarge', 4, 61, 'High', 1.649, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(211, 'EC2', 'LINUX', 'p3.16xlarge', 64, 488, '25 Gigabit', 33.872, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(212, 'EC2', 'WINDOWS', 'p3.16xlarge', 64, 488, '25 Gigabit', 36.816, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(213, 'EC2', 'WINDOWS', 'p3.2xlarge', 8, 61, 'Up to 10 Gigabit', 4.602, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(214, 'EC2', 'LINUX', 'p3.2xlarge', 8, 61, 'Up to 10 Gigabit', 4.234, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(215, 'EC2', 'WINDOWS', 'p3.8xlarge', 32, 244, '10 Gigabit', 18.408, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(216, 'EC2', 'LINUX', 'p3.8xlarge', 32, 244, '10 Gigabit', 16.936, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(217, 'EC2', 'WINDOWS', 'r3.2xlarge', 8, 61, 'High', 1.166, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(218, 'EC2', 'LINUX', 'r3.2xlarge', 8, 61, 'High', 0.798, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(219, 'EC2', 'LINUX', 'r3.4xlarge', 16, 122, 'High', 1.596, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(220, 'EC2', 'WINDOWS', 'r3.4xlarge', 16, 122, 'High', 2.332, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(221, 'EC2', 'LINUX', 'r3.8xlarge', 32, 244, '10 Gigabit', 3.192, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(222, 'EC2', 'WINDOWS', 'r3.8xlarge', 32, 244, '10 Gigabit', 4.664, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(223, 'EC2', 'WINDOWS', 'r3.large', 2, 15, 'Moderate', 0.292, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(224, 'EC2', 'LINUX', 'r3.large', 2, 15, 'Moderate', 0.2, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(225, 'EC2', 'WINDOWS', 'r3.xlarge', 4, 30, 'Moderate', 0.583, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(226, 'EC2', 'LINUX', 'r3.xlarge', 4, 30, 'Moderate', 0.399, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(227, 'EC2', 'LINUX', 'r4.16xlarge', 64, 488, '20 Gigabit', 5.12, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(228, 'EC2', 'WINDOWS', 'r4.16xlarge', 64, 488, '20 Gigabit', 8.064, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(229, 'EC2', 'WINDOWS', 'r4.2xlarge', 8, 61, 'Up to 10 Gigabit', 1.008, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(230, 'EC2', 'LINUX', 'r4.2xlarge', 8, 61, 'Up to 10 Gigabit', 0.64, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(231, 'EC2', 'LINUX', 'r4.4xlarge', 16, 122, 'Up to 10 Gigabit', 1.28, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(232, 'EC2', 'WINDOWS', 'r4.4xlarge', 16, 122, 'Up to 10 Gigabit', 2.016, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(233, 'EC2', 'LINUX', 'r4.8xlarge', 32, 244, '10 Gigabit', 2.56, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(234, 'EC2', 'WINDOWS', 'r4.8xlarge', 32, 244, '10 Gigabit', 4.032, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(235, 'EC2', 'LINUX', 'r4.large', 2, 15, 'Up to 10 Gigabit', 0.16, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(236, 'EC2', 'WINDOWS', 'r4.large', 2, 15, 'Up to 10 Gigabit', 0.252, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(237, 'EC2', 'LINUX', 'r4.xlarge', 4, 30, 'Up to 10 Gigabit', 0.32, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(238, 'EC2', 'WINDOWS', 'r4.xlarge', 4, 30, 'Up to 10 Gigabit', 0.504, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(239, 'EC2', 'WINDOWS', 'r5.12xlarge', 48, 384, '10 Gigabit', 5.856, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(240, 'EC2', 'LINUX', 'r5.12xlarge', 48, 384, '10 Gigabit', 3.648, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(241, 'EC2', 'WINDOWS', 'r5.16xlarge', 64, 512, '20 Gigabit', 7.808, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(242, 'EC2', 'LINUX', 'r5.16xlarge', 64, 512, '20 Gigabit', 4.864, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(243, 'EC2', 'LINUX', 'r5.24xlarge', 96, 768, '25 Gigabit', 7.296, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(244, 'EC2', 'WINDOWS', 'r5.24xlarge', 96, 768, '25 Gigabit', 11.712, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(245, 'EC2', 'LINUX', 'r5.2xlarge', 8, 64, 'Up to 10 Gigabit', 0.608, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(246, 'EC2', 'WINDOWS', 'r5.2xlarge', 8, 64, 'Up to 10 Gigabit', 0.976, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(247, 'EC2', 'WINDOWS', 'r5.4xlarge', 16, 128, 'Up to 10 Gigabit', 1.952, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(248, 'EC2', 'LINUX', 'r5.4xlarge', 16, 128, 'Up to 10 Gigabit', 1.216, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(249, 'EC2', 'WINDOWS', 'r5.8xlarge', 32, 256, '10 Gigabit', 3.904, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(250, 'EC2', 'LINUX', 'r5.8xlarge', 32, 256, '10 Gigabit', 2.432, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(251, 'EC2', 'WINDOWS', 'r5.large', 2, 16, 'Up to 10 Gigabit', 0.244, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(252, 'EC2', 'LINUX', 'r5.large', 2, 16, 'Up to 10 Gigabit', 0.152, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(253, 'EC2', 'LINUX', 'r5.metal', 96, 768, '25 Gigabit', 7.296, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(254, 'EC2', 'WINDOWS', 'r5.metal', 96, 768, '25 Gigabit', 11.712, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(255, 'EC2', 'WINDOWS', 'r5.xlarge', 4, 32, 'Up to 10 Gigabit', 0.488, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(256, 'EC2', 'LINUX', 'r5.xlarge', 4, 32, 'Up to 10 Gigabit', 0.304, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(257, 'EC2', 'LINUX', 'r5a.12xlarge', 48, 384, '10 Gigabit', 3.264, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(258, 'EC2', 'WINDOWS', 'r5a.12xlarge', 48, 384, '10 Gigabit', 5.472, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(259, 'EC2', 'WINDOWS', 'r5a.16xlarge', 64, 512, '12 Gigabit', 7.296, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(260, 'EC2', 'LINUX', 'r5a.16xlarge', 64, 512, '12 Gigabit', 4.352, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(261, 'EC2', 'WINDOWS', 'r5a.24xlarge', 96, 768, '20 Gigabit', 10.944, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(262, 'EC2', 'LINUX', 'r5a.24xlarge', 96, 768, '20 Gigabit', 6.528, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(263, 'EC2', 'WINDOWS', 'r5a.2xlarge', 8, 64, '10 Gigabit', 0.912, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(264, 'EC2', 'LINUX', 'r5a.2xlarge', 8, 64, '10 Gigabit', 0.544, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(265, 'EC2', 'LINUX', 'r5a.4xlarge', 16, 128, '10 Gigabit', 1.088, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(266, 'EC2', 'WINDOWS', 'r5a.4xlarge', 16, 128, '10 Gigabit', 1.824, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(267, 'EC2', 'WINDOWS', 'r5a.8xlarge', 32, 256, 'Up to 10 Gigabit', 3.648, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(268, 'EC2', 'LINUX', 'r5a.8xlarge', 32, 256, 'Up to 10 Gigabit', 2.176, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(269, 'EC2', 'LINUX', 'r5a.large', 2, 16, '10 Gigabit', 0.136, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(270, 'EC2', 'WINDOWS', 'r5a.large', 2, 16, '10 Gigabit', 0.228, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(271, 'EC2', 'WINDOWS', 'r5a.xlarge', 4, 32, '10 Gigabit', 0.456, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(272, 'EC2', 'LINUX', 'r5a.xlarge', 4, 32, '10 Gigabit', 0.272, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(273, 'EC2', 'WINDOWS', 'r5ad.12xlarge', 48, 384, '10 Gigabit', 6.0, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(274, 'EC2', 'LINUX', 'r5ad.12xlarge', 48, 384, '10 Gigabit', 3.792, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(275, 'EC2', 'WINDOWS', 'r5ad.16xlarge', 64, 512, '12 Gigabit', 8.0, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(276, 'EC2', 'LINUX', 'r5ad.16xlarge', 64, 512, '12 Gigabit', 5.056, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(277, 'EC2', 'LINUX', 'r5ad.24xlarge', 96, 768, '20 Gigabit', 7.584, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(278, 'EC2', 'WINDOWS', 'r5ad.24xlarge', 96, 768, '20 Gigabit', 12.0, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(279, 'EC2', 'LINUX', 'r5ad.2xlarge', 8, 64, '10 Gigabit', 0.632, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(280, 'EC2', 'WINDOWS', 'r5ad.2xlarge', 8, 64, '10 Gigabit', 1.0, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(281, 'EC2', 'LINUX', 'r5ad.4xlarge', 16, 128, '10 Gigabit', 1.264, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(282, 'EC2', 'WINDOWS', 'r5ad.4xlarge', 16, 128, '10 Gigabit', 2.0, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(283, 'EC2', 'LINUX', 'r5ad.8xlarge', 32, 256, 'Up to 10 Gigabit', 2.528, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(284, 'EC2', 'WINDOWS', 'r5ad.8xlarge', 32, 256, 'Up to 10 Gigabit', 4.0, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(285, 'EC2', 'LINUX', 'r5ad.large', 2, 16, '10 Gigabit', 0.158, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(286, 'EC2', 'WINDOWS', 'r5ad.large', 2, 16, '10 Gigabit', 0.25, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(287, 'EC2', 'WINDOWS', 'r5ad.xlarge', 4, 32, '10 Gigabit', 0.5, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(288, 'EC2', 'LINUX', 'r5ad.xlarge', 4, 32, '10 Gigabit', 0.316, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(289, 'EC2', 'LINUX', 'r5d.12xlarge', 48, 384, '10 Gigabit', 4.152, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(290, 'EC2', 'WINDOWS', 'r5d.12xlarge', 48, 384, '10 Gigabit', 6.36, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(291, 'EC2', 'WINDOWS', 'r5d.16xlarge', 64, 512, '20 Gigabit', 8.48, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(292, 'EC2', 'LINUX', 'r5d.16xlarge', 64, 512, '20 Gigabit', 5.536, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(293, 'EC2', 'LINUX', 'r5d.24xlarge', 96, 768, '25 Gigabit', 8.304, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(294, 'EC2', 'WINDOWS', 'r5d.24xlarge', 96, 768, '25 Gigabit', 12.72, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(295, 'EC2', 'LINUX', 'r5d.2xlarge', 8, 64, '10 Gigabit', 0.692, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(296, 'EC2', 'WINDOWS', 'r5d.2xlarge', 8, 64, '10 Gigabit', 1.06, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(297, 'EC2', 'WINDOWS', 'r5d.4xlarge', 16, 128, '10 Gigabit', 2.12, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(298, 'EC2', 'LINUX', 'r5d.4xlarge', 16, 128, '10 Gigabit', 1.384, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(299, 'EC2', 'WINDOWS', 'r5d.8xlarge', 32, 256, '10 Gigabit', 4.24, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(300, 'EC2', 'LINUX', 'r5d.8xlarge', 32, 256, '10 Gigabit', 2.768, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(301, 'EC2', 'WINDOWS', 'r5d.large', 2, 16, '10 Gigabit', 0.265, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(302, 'EC2', 'LINUX', 'r5d.large', 2, 16, '10 Gigabit', 0.173, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(303, 'EC2', 'LINUX', 'r5d.metal', 96, 768, '25 Gigabit', 8.304, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(304, 'EC2', 'WINDOWS', 'r5d.metal', 96, 768, '25 Gigabit', 12.72, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(305, 'EC2', 'LINUX', 'r5d.xlarge', 4, 32, '10 Gigabit', 0.346, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(306, 'EC2', 'WINDOWS', 'r5d.xlarge', 4, 32, '10 Gigabit', 0.53, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(307, 'EC2', 'LINUX', 't2.2xlarge', 8, 32, 'Moderate', 0.4608, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(308, 'EC2', 'WINDOWS', 't2.2xlarge', 8, 32, 'Moderate', 0.5228, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(309, 'EC2', 'WINDOWS', 't2.large', 2, 8, 'Low to Moderate', 0.1432, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(310, 'EC2', 'LINUX', 't2.large', 2, 8, 'Low to Moderate', 0.1152, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(311, 'EC2', 'WINDOWS', 't2.medium', 2, 4, 'Low to Moderate', 0.0756, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(312, 'EC2', 'LINUX', 't2.medium', 2, 4, 'Low to Moderate', 0.0576, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(313, 'EC2', 'WINDOWS', 't2.micro', 1, 1, 'Low to Moderate', 0.019, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(314, 'EC2', 'LINUX', 't2.micro', 1, 1, 'Low to Moderate', 0.0144, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(315, 'EC2', 'LINUX', 't2.nano', 1, 0, 'Low', 0.0072, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(316, 'EC2', 'WINDOWS', 't2.nano', 1, 0, 'Low', 0.0095, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(317, 'EC2', 'LINUX', 't2.small', 1, 2, 'Low to Moderate', 0.0288, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(318, 'EC2', 'WINDOWS', 't2.small', 1, 2, 'Low to Moderate', 0.038, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(319, 'EC2', 'LINUX', 't2.xlarge', 4, 16, 'Moderate', 0.2304, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(320, 'EC2', 'WINDOWS', 't2.xlarge', 4, 16, 'Moderate', 0.2714, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(321, 'EC2', 'LINUX', 't3.2xlarge', 8, 32, 'Moderate', 0.416, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(322, 'EC2', 'WINDOWS', 't3.2xlarge', 8, 32, 'Moderate', 0.5632, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(323, 'EC2', 'LINUX', 't3.large', 2, 8, 'Low to Moderate', 0.104, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(324, 'EC2', 'WINDOWS', 't3.large', 2, 8, 'Low to Moderate', 0.1316, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(325, 'EC2', 'LINUX', 't3.medium', 2, 4, 'Low to Moderate', 0.052, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(326, 'EC2', 'WINDOWS', 't3.medium', 2, 4, 'Low to Moderate', 0.0704, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(327, 'EC2', 'LINUX', 't3.micro', 2, 1, 'Low to Moderate', 0.013, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(328, 'EC2', 'WINDOWS', 't3.micro', 2, 1, 'Low to Moderate', 0.0222, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(329, 'EC2', 'WINDOWS', 't3.nano', 2, 0, 'Low', 0.0111, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(330, 'EC2', 'LINUX', 't3.nano', 2, 0, 'Low', 0.0065, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(331, 'EC2', 'LINUX', 't3.small', 2, 2, 'Low to Moderate', 0.026, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(332, 'EC2', 'WINDOWS', 't3.small', 2, 2, 'Low to Moderate', 0.0444, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(333, 'EC2', 'LINUX', 't3.xlarge', 4, 16, 'Moderate', 0.208, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(334, 'EC2', 'WINDOWS', 't3.xlarge', 4, 16, 'Moderate', 0.2816, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(335, 'EC2', 'WINDOWS', 't3a.2xlarge', 8, 32, 'Moderate', 0.5216, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(336, 'EC2', 'LINUX', 't3a.2xlarge', 8, 32, 'Moderate', 0.3744, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(337, 'EC2', 'WINDOWS', 't3a.large', 2, 8, 'Low to Moderate', 0.1212, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(338, 'EC2', 'LINUX', 't3a.large', 2, 8, 'Low to Moderate', 0.0936, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(339, 'EC2', 'WINDOWS', 't3a.medium', 2, 4, 'Low to Moderate', 0.0652, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(340, 'EC2', 'LINUX', 't3a.medium', 2, 4, 'Low to Moderate', 0.0468, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(341, 'EC2', 'WINDOWS', 't3a.micro', 2, 1, 'Low to Moderate', 0.0209, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(342, 'EC2', 'LINUX', 't3a.micro', 2, 1, 'Low to Moderate', 0.0117, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(343, 'EC2', 'LINUX', 't3a.nano', 2, 0, 'Low', 0.0059, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(344, 'EC2', 'WINDOWS', 't3a.nano', 2, 0, 'Low', 0.0105, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(345, 'EC2', 'WINDOWS', 't3a.small', 2, 2, 'Low to Moderate', 0.0418, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(346, 'EC2', 'LINUX', 't3a.small', 2, 2, 'Low to Moderate', 0.0234, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(347, 'EC2', 'WINDOWS', 't3a.xlarge', 4, 16, 'Moderate', 0.2608, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(348, 'EC2', 'LINUX', 't3a.xlarge', 4, 16, 'Moderate', 0.1872, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(349, 'EC2', 'LINUX', 'x1.16xlarge', 64, 976, 'High', 9.671, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(350, 'EC2', 'WINDOWS', 'x1.16xlarge', 64, 976, 'High', 12.615, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(351, 'EC2', 'WINDOWS', 'x1.32xlarge', 128, 1952, 'High', 25.229, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(352, 'EC2', 'LINUX', 'x1.32xlarge', 128, 1952, 'High', 19.341, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(353, 'EC2', 'LINUX', 'x1e.16xlarge', 64, 1952, '10 Gigabit', 19.344, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(354, 'EC2', 'WINDOWS', 'x1e.16xlarge', 64, 1952, '10 Gigabit', 22.288, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(355, 'EC2', 'LINUX', 'x1e.2xlarge', 8, 244, 'Up to 10 Gigabit', 2.418, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(356, 'EC2', 'WINDOWS', 'x1e.2xlarge', 8, 244, 'Up to 10 Gigabit', 2.786, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(357, 'EC2', 'WINDOWS', 'x1e.32xlarge', 128, 3904, '25 Gigabit', 44.576, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(358, 'EC2', 'LINUX', 'x1e.32xlarge', 128, 3904, '25 Gigabit', 38.688, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(359, 'EC2', 'LINUX', 'x1e.4xlarge', 16, 488, 'Up to 10 Gigabit', 4.836, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(360, 'EC2', 'WINDOWS', 'x1e.4xlarge', 16, 488, 'Up to 10 Gigabit', 5.572, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(361, 'EC2', 'WINDOWS', 'x1e.8xlarge', 32, 976, 'Up to 10 Gigabit', 11.144, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(362, 'EC2', 'LINUX', 'x1e.8xlarge', 32, 976, 'Up to 10 Gigabit', 9.672, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(363, 'EC2', 'WINDOWS', 'x1e.xlarge', 4, 122, 'Up to 10 Gigabit', 1.393, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(364, 'EC2', 'LINUX', 'x1e.xlarge', 4, 122, 'Up to 10 Gigabit', 1.209, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(365, 'EC2', 'WINDOWS', 'z1d.12xlarge', 48, 384, '25 Gigabit', 7.608, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(366, 'EC2', 'LINUX', 'z1d.12xlarge', 48, 384, '25 Gigabit', 5.4, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(367, 'EC2', 'LINUX', 'z1d.2xlarge', 8, 64, 'Up to 10 Gigabit', 0.9, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(368, 'EC2', 'WINDOWS', 'z1d.2xlarge', 8, 64, 'Up to 10 Gigabit', 1.268, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(369, 'EC2', 'LINUX', 'z1d.3xlarge', 12, 96, 'Up to 10 Gigabit', 1.35, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(370, 'EC2', 'WINDOWS', 'z1d.3xlarge', 12, 96, 'Up to 10 Gigabit', 1.902, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(371, 'EC2', 'WINDOWS', 'z1d.6xlarge', 24, 192, '10 Gigabit', 3.804, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(372, 'EC2', 'LINUX', 'z1d.6xlarge', 24, 192, '10 Gigabit', 2.7, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(373, 'EC2', 'WINDOWS', 'z1d.large', 2, 16, 'Up to 10 Gigabit', 0.317, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(374, 'EC2', 'LINUX', 'z1d.large', 2, 16, 'Up to 10 Gigabit', 0.225, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(375, 'EC2', 'WINDOWS', 'z1d.metal', 48, 384, '25 Gigabit', 7.608, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(376, 'EC2', 'LINUX', 'z1d.metal', 48, 384, '25 Gigabit', 5.4, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(377, 'EC2', 'WINDOWS', 'z1d.xlarge', 4, 32, 'Up to 10 Gigabit', 0.634, 'ACTIVE', '2020-05-26 13:46:00.000', NULL)
     ,(378, 'EC2', 'LINUX', 'z1d.xlarge', 4, 32, 'Up to 10 Gigabit', 0.45, 'ACTIVE', '2020-05-26 13:46:00.000', NULL);


CREATE TABLE `agent_logs` (
                              `idx` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
                              `tx_id` VARCHAR(100) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
                              `user_idx` BIGINT(20) NULL DEFAULT '0',
                              `client_ip` INT(10) NULL DEFAULT NULL,
                              `headers` TEXT NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
                              `method` VARCHAR(20) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
                              `params` TEXT NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
                              `created_at` DATETIME NULL DEFAULT NULL,
                              `updated_at` DATETIME NULL DEFAULT NULL,
                              PRIMARY KEY (`idx`) USING BTREE
)
    COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;
CREATE TABLE `meta_master` (
                               `dbms_type` VARCHAR(100) NOT NULL COLLATE 'utf8mb4_general_ci',
                               `object_id` VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
                               `schema_name` VARCHAR(100) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
                               `object_name` VARCHAR(100) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
                               `table_description` VARCHAR(500) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
                               `column_no` INT(4) NULL DEFAULT NULL,
                               `column_name` VARCHAR(200) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
                               `data_type` VARCHAR(100) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
                               `encoding` VARCHAR(100) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
                               `dist_key` VARCHAR(100) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
                               `sort_key` VARCHAR(100) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
                               `notnull_flag` VARCHAR(100) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
                               `column_description` VARCHAR(500) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
                               `load_master_at` DATETIME NULL DEFAULT NULL,
                               `change_yn` ENUM('Y','N') NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci'
)
    COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;

-- INSERT INTO `users` (`idx`, `login_id`, `login_pw`, `nickname`, `user_status`, `user_grade`, `created_at`, `updated_at`, `last_login_at`, `password_error_count`) VALUES (2, 'admin', '{bcrypt}$2a$10$5fOVxpFd45TAJLoZcrV3SO9cJqLJBS646GI7apHr7qHr.lSAlSb6S', 'hi3', 'ACTIVE', 'ROLE_ADMIN', '2021-03-10 13:10:16', '2021-05-28 05:05:41', '2021-06-04 01:21:10', 0);