SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;
SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0;
SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema teachsync
-- -----------------------------------------------------

DROP SCHEMA IF EXISTS `teachsync`;

CREATE SCHEMA IF NOT EXISTS `teachsync`;
USE `teachsync`;


-- -----------------------------------------------------
-- Table `teachsync`.`location_unit`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teachsync`.`location_unit`;

CREATE TABLE IF NOT EXISTS `teachsync`.`location_unit`
(
    `id`        BIGINT       NOT NULL AUTO_INCREMENT,
    `parentId`  BIGINT       NULL DEFAULT NULL,
    `level`     INT          NOT NULL COMMENT 'Start at 0 (COUNTRY)',
    `unitName`  VARCHAR(255) NOT NULL COMMENT 'VD: Thanh Pho Ho Chi Minh',
    `unitAlias` VARCHAR(45)  NOT NULL COMMENT 'VD: Tp.HMC',
    `unitType`  VARCHAR(45)  NOT NULL COMMENT 'COUNTRY, PROVINCE, CITY, ...',
    `status`    VARCHAR(45)  NOT NULL,
    `createdAt` DATETIME     NULL DEFAULT NULL,
    `createdBy` BIGINT       NULL DEFAULT NULL,
    `updatedAt` DATETIME     NULL DEFAULT NULL,
    `updatedBy` BIGINT       NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_location_unit_location_unit_parentId_idx` (`parentId` ASC) VISIBLE,
    INDEX `fk_location_unit_user_createdBy_idx` (`createdBy` ASC) VISIBLE,
    INDEX `fk_location_unit_user_updatedBy_idx` (`updatedBy` ASC) VISIBLE,
    CONSTRAINT `fk_location_unit_location_unit_parentId`
        FOREIGN KEY (`parentId`)
            REFERENCES `teachsync`.`location_unit` (`id`),
    CONSTRAINT `fk_location_unit_user_createdBy`
        FOREIGN KEY (`createdBy`)
            REFERENCES `teachsync`.`user` (`id`),
    CONSTRAINT `fk_location_unit_user_updatedBy`
        FOREIGN KEY (`updatedBy`)
            REFERENCES `teachsync`.`user` (`id`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teachsync`.`address`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teachsync`.`address`;

CREATE TABLE IF NOT EXISTS `teachsync`.`address`
(
    `id`            BIGINT       NOT NULL AUTO_INCREMENT,
    `addressNo`     VARCHAR(45)  NOT NULL,
    `street`        VARCHAR(255) NOT NULL,
    `unitId`        BIGINT       NOT NULL COMMENT 'LocationUnit id of largest level applicable',
    `addressString` LONGTEXT     NULL DEFAULT NULL COMMENT 'auto generated in code, save on query resource',
    `status`        VARCHAR(45)  NOT NULL,
    `createdAt`     DATETIME     NULL DEFAULT NULL,
    `createdBy`     BIGINT       NULL DEFAULT NULL,
    `updatedAt`     DATETIME     NULL DEFAULT NULL,
    `updatedBy`     BIGINT       NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_address_location_unit_idx` (`unitId` ASC) VISIBLE,
    INDEX `fk_address_user_createdBy_idx` (`createdBy` ASC) VISIBLE,
    INDEX `fk_address_user_updatedBy_idx` (`updatedBy` ASC) VISIBLE,
    CONSTRAINT `fk_address_location_unit`
        FOREIGN KEY (`unitId`)
            REFERENCES `teachsync`.`location_unit` (`id`),
    CONSTRAINT `fk_address_user_createdBy`
        FOREIGN KEY (`createdBy`)
            REFERENCES `teachsync`.`user` (`id`),
    CONSTRAINT `fk_address_user_updatedBy`
        FOREIGN KEY (`updatedBy`)
            REFERENCES `teachsync`.`user` (`id`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teachsync`.`role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teachsync`.`role`;

CREATE TABLE IF NOT EXISTS `teachsync`.`role`
(
    `id`        BIGINT      NOT NULL AUTO_INCREMENT,
    `roleName`  VARCHAR(45) NOT NULL,
    `roleDesc`  LONGTEXT    NULL DEFAULT NULL,
    `status`    VARCHAR(45) NOT NULL,
    `createdAt` DATETIME    NULL DEFAULT NULL,
    `createdBy` BIGINT      NULL DEFAULT NULL,
    `updatedAt` DATETIME    NULL DEFAULT NULL,
    `updatedBy` BIGINT      NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_role_user_createdBy_idx` (`createdBy` ASC) VISIBLE,
    INDEX `fk_role_user_updatedBy_idx` (`updatedBy` ASC) VISIBLE,
    CONSTRAINT `fk_role_user_createdBy`
        FOREIGN KEY (`createdBy`)
            REFERENCES `teachsync`.`user` (`id`),
    CONSTRAINT `fk_role_user_updatedBy`
        FOREIGN KEY (`updatedBy`)
            REFERENCES `teachsync`.`user` (`id`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teachsync`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teachsync`.`user`;

CREATE TABLE IF NOT EXISTS `teachsync`.`user`
(
    `id`                 BIGINT       NOT NULL AUTO_INCREMENT,
    `username`           VARCHAR(45)  NOT NULL,
    `password`           VARCHAR(255) NOT NULL,
    `roleId`             BIGINT       NOT NULL,
    `userAvatar`         LONGTEXT     NULL DEFAULT NULL COMMENT 'Link to avatar',
    `fullName`           VARCHAR(255) NOT NULL,
    `email`              VARCHAR(255) NOT NULL,
    `phone`              VARCHAR(10)  NULL DEFAULT NULL,
    `addressId`          BIGINT       NULL DEFAULT NULL,
    `resetPasswordToken` VARCHAR(255) NULL DEFAULT NULL,
    `parentId`           BIGINT       NULL DEFAULT NULL,
    `status`             VARCHAR(45)  NOT NULL,
    `createdAt`          DATETIME     NULL DEFAULT NULL,
    `createdBy`          BIGINT       NULL DEFAULT NULL,
    `updatedAt`          DATETIME     NULL DEFAULT NULL,
    `updatedBy`          BIGINT       NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_user_address_idx` (`addressId` ASC) VISIBLE,
    INDEX `fk_user_user_parentId_idx` (`parentId` ASC) VISIBLE,
    INDEX `fk_user_role_idx` (`roleId` ASC) VISIBLE,
    INDEX `fk_user_user_createdBy_idx` (`createdBy` ASC) VISIBLE,
    INDEX `fk_user_user_updatedBy_idx` (`updatedBy` ASC) VISIBLE,
    CONSTRAINT `fk_user_address`
        FOREIGN KEY (`addressId`)
            REFERENCES `teachsync`.`address` (`id`),
    CONSTRAINT `fk_user_role`
        FOREIGN KEY (`roleId`)
            REFERENCES `teachsync`.`role` (`id`),
    CONSTRAINT `fk_user_user_parentId`
        FOREIGN KEY (`parentId`)
            REFERENCES `teachsync`.`user` (`id`),
    CONSTRAINT `fk_user_user_createdBy`
        FOREIGN KEY (`createdBy`)
            REFERENCES `teachsync`.`user` (`id`),
    CONSTRAINT `fk_user_user_updatedBy`
        FOREIGN KEY (`updatedBy`)
            REFERENCES `teachsync`.`user` (`id`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teachsync`.`center`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teachsync`.`center`;

CREATE TABLE IF NOT EXISTS `teachsync`.`center`
(
    `id`         BIGINT      NOT NULL AUTO_INCREMENT,
    `addressId`  BIGINT      NOT NULL,
    `centerName` VARCHAR(45) NOT NULL,
    `centerType` VARCHAR(45) NOT NULL COMMENT 'Center Specialization: ENGLISH, FRENCH, MIXED, ...',
    `centerDesc` LONGTEXT    NULL DEFAULT NULL,
    `centerSize` INT         NOT NULL COMMENT 'Number of Room (max for room input)',
    `status`     VARCHAR(45) NOT NULL,
    `createdAt`  DATETIME    NULL DEFAULT NULL,
    `createdBy`  BIGINT      NULL DEFAULT NULL,
    `updatedAt`  DATETIME    NULL DEFAULT NULL,
    `updatedBy`  BIGINT      NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_center_address_idx` (`addressId` ASC) VISIBLE,
    INDEX `fk_center_user_createdBy_idx` (`createdBy` ASC) VISIBLE,
    INDEX `fk_center_user_updatedBy_idx` (`updatedBy` ASC) VISIBLE,
    CONSTRAINT `fk_center_address`
        FOREIGN KEY (`addressId`)
            REFERENCES `teachsync`.`address` (`id`),
    CONSTRAINT `fk_center_user_createdBy`
        FOREIGN KEY (`createdBy`)
            REFERENCES `teachsync`.`user` (`id`),
    CONSTRAINT `fk_center_user_updatedBy`
        FOREIGN KEY (`updatedBy`)
            REFERENCES `teachsync`.`user` (`id`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teachsync`.`room`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teachsync`.`room`;

CREATE TABLE IF NOT EXISTS `teachsync`.`room`
(
    `id`        BIGINT      NOT NULL AUTO_INCREMENT,
    `centerId`  BIGINT      NOT NULL,
    `roomType`  VARCHAR(45) NULL DEFAULT NULL COMMENT 'CLASSROOM, LIBRARY, ...',
    `roomDesc`  LONGTEXT    NULL DEFAULT NULL,
    `roomName`  VARCHAR(45) NULL DEFAULT NULL,
    `roomSize`  INT         NOT NULL COMMENT 'Number of people supported. VD: Phòng học tối đa 20 chỗ ngồi',
    `status`    VARCHAR(45) NOT NULL,
    `createdAt` DATETIME    NULL DEFAULT NULL,
    `createdBy` BIGINT      NULL DEFAULT NULL,
    `updatedAt` DATETIME    NULL DEFAULT NULL,
    `updatedBy` BIGINT      NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_room_center_idx` (`centerId` ASC) VISIBLE,
    INDEX `fk_room_user_createdBy_idx` (`createdBy` ASC) VISIBLE,
    INDEX `fk_room_user_updatedBy_idx` (`updatedBy` ASC) VISIBLE,
    CONSTRAINT `fk_room_center`
        FOREIGN KEY (`centerId`)
            REFERENCES `teachsync`.`center` (`id`),
    CONSTRAINT `fk_room_user_createdBy`
        FOREIGN KEY (`createdBy`)
            REFERENCES `teachsync`.`user` (`id`),
    CONSTRAINT `fk_room_user_updatedBy`
        FOREIGN KEY (`updatedBy`)
            REFERENCES `teachsync`.`user` (`id`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teachsync`.`staff`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teachsync`.`staff`;

CREATE TABLE IF NOT EXISTS `teachsync`.`staff`
(
    `id`        BIGINT      NOT NULL AUTO_INCREMENT,
    `centerId`  BIGINT      NOT NULL,
    `userId`    BIGINT      NOT NULL,
    `staffType` VARCHAR(45) NOT NULL COMMENT 'TEACHER, DEAN, ...',
    `status`    VARCHAR(45) NOT NULL,
    `createdAt` DATETIME    NULL DEFAULT NULL,
    `createdBy` BIGINT      NULL DEFAULT NULL,
    `updatedAt` DATETIME    NULL DEFAULT NULL,
    `updatedBy` BIGINT      NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_staff_center_idx` (`centerId` ASC) VISIBLE,
    INDEX `fk_staff_user_idx` (`userId` ASC) VISIBLE,
    INDEX `fk_staff_user_createdBy_idx` (`createdBy` ASC) VISIBLE,
    INDEX `fk_staff_user_updatedBy_idx` (`updatedBy` ASC) VISIBLE,
    CONSTRAINT `fk_staff_center`
        FOREIGN KEY (`centerId`)
            REFERENCES `teachsync`.`center` (`id`),
    CONSTRAINT `fk_staff_user`
        FOREIGN KEY (`userId`)
            REFERENCES `teachsync`.`user` (`id`),
    CONSTRAINT `fk_staff_user_createdBy`
        FOREIGN KEY (`createdBy`)
            REFERENCES `teachsync`.`user` (`id`),
    CONSTRAINT `fk_staff_user_updatedBy`
        FOREIGN KEY (`updatedBy`)
            REFERENCES `teachsync`.`user` (`id`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teachsync`.`course`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teachsync`.`course`;

CREATE TABLE IF NOT EXISTS `teachsync`.`course`
(
    `id`           BIGINT      NOT NULL AUTO_INCREMENT,
    `courseName`   VARCHAR(45) NOT NULL,
    `courseAlias`  VARCHAR(10) NOT NULL,
    `courseImg`    LONGTEXT    NOT NULL COMMENT 'Link to image',
    `courseDesc`   LONGTEXT    NULL DEFAULT NULL,
    `numSession`   INT         NOT NULL COMMENT 'Number of total session (NOT LESSON)',
    `minScore`     FLOAT       NOT NULL COMMENT 'Min total score needed to pass',
    `minAttendant` FLOAT       NOT NULL COMMENT 'Min % of total attendant needed to pass',
    `status`       VARCHAR(45) NOT NULL,
    `createdAt`    DATETIME    NULL DEFAULT NULL,
    `createdBy`    BIGINT      NULL DEFAULT NULL,
    `updatedAt`    DATETIME    NULL DEFAULT NULL,
    `updatedBy`    BIGINT      NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_course_user_createdBy_idx` (`createdBy` ASC) VISIBLE,
    INDEX `fk_course_user_updatedBy_idx` (`updatedBy` ASC) VISIBLE,
    CONSTRAINT `fk_course_user_createdBy`
        FOREIGN KEY (`createdBy`)
            REFERENCES `teachsync`.`user` (`id`),
    CONSTRAINT `fk_course_user_updatedBy`
        FOREIGN KEY (`updatedBy`)
            REFERENCES `teachsync`.`user` (`id`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teachsync`.`price_log`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teachsync`.`price_log`;

CREATE TABLE IF NOT EXISTS `teachsync`.`price_log`
(
    `id`              BIGINT      NOT NULL AUTO_INCREMENT,
    `courseId`        BIGINT      NOT NULL,
    `price`           DOUBLE      NOT NULL,
    `isPromotion`     BIT(1)      NOT NULL COMMENT 'Có giảm giá hay không',
    `promotionAmount` DOUBLE      NULL DEFAULT NULL COMMENT 'Chỉ ghi khi isPromotion = true. Giảm bao nhiêu',
    `promotionType`   VARCHAR(45) NULL DEFAULT NULL COMMENT 'Chỉ ghi khi isPromotion = true. Giảm % hay giảm số tiền cụ thể',
    `promotionDesc`   LONGTEXT    NULL DEFAULT NULL COMMENT 'Chỉ ghi khi isPromotion = true. Chi tiết chương trình giảm giá',
    `validFrom`       DATETIME    NOT NULL COMMENT 'Mặc định thời giờ lúc nhập dữ liệu',
    `validTo`         DATETIME    NULL DEFAULT NULL COMMENT 'Mặc định null, cập nhập = validTo giá tiếp theo',
    `status`          VARCHAR(45) NOT NULL,
    `createdAt`       DATETIME    NULL DEFAULT NULL,
    `createdBy`       BIGINT      NULL DEFAULT NULL,
    `updatedAt`       DATETIME    NULL DEFAULT NULL,
    `updatedBy`       BIGINT      NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_price_log_course_idx` (`courseId` ASC) VISIBLE,
    INDEX `fk_price_log_user_createdBy_idx` (`createdBy` ASC) VISIBLE,
    INDEX `fk_price_log_user_updatedBy_idx` (`updatedBy` ASC) VISIBLE,
    CONSTRAINT `fk_price_log_course`
        FOREIGN KEY (`courseId`)
            REFERENCES `teachsync`.`course` (`id`),
    CONSTRAINT `fk_price_log_user_createdBy`
        FOREIGN KEY (`createdBy`)
            REFERENCES `teachsync`.`user` (`id`),
    CONSTRAINT `fk_price_log_user_updatedBy`
        FOREIGN KEY (`updatedBy`)
            REFERENCES `teachsync`.`user` (`id`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teachsync`.`material`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teachsync`.`material`;

CREATE TABLE IF NOT EXISTS `teachsync`.`material`
(
    `id`              BIGINT      NOT NULL AUTO_INCREMENT,
    `materialName`    VARCHAR(45) NOT NULL,
    `materialImg`     LONGTEXT    NOT NULL COMMENT 'Link to image',
    `materialLink`    LONGTEXT    NULL DEFAULT NULL COMMENT 'Chứa link tới file',
    `materialContent` MEDIUMBLOB  NULL DEFAULT NULL COMMENT 'Chứa file trong DB',
    `materialType`    VARCHAR(45) NOT NULL COMMENT '(Tạm thời chỉ có các dạng digital) E_BOOK, PDF, WORD, EXCEL, ...',
    `isFree`          BIT(1)      NOT NULL DEFAULT true COMMENT 'can it be view by guest?',
    `status`          VARCHAR(45) NOT NULL,
    `createdAt`       DATETIME    NULL DEFAULT NULL,
    `createdBy`       BIGINT      NULL DEFAULT NULL,
    `updatedAt`       DATETIME    NULL DEFAULT NULL,
    `updatedBy`       BIGINT      NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_material_user_createdBy_idx` (`createdBy` ASC) VISIBLE,
    INDEX `fk_material_user_updatedBy_idx` (`updatedBy` ASC) VISIBLE,
    CONSTRAINT `fk_material_user_createdBy`
        FOREIGN KEY (`createdBy`)
            REFERENCES `teachsync`.`user` (`id`),
    CONSTRAINT `fk_material_user_updatedBy`
        FOREIGN KEY (`updatedBy`)
            REFERENCES `teachsync`.`user` (`id`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teachsync`.`course_material`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teachsync`.`course_material`;

CREATE TABLE IF NOT EXISTS `teachsync`.`course_material`
(
    `id`         BIGINT      NOT NULL AUTO_INCREMENT,
    `courseId`   BIGINT      NOT NULL,
    `materialId` BIGINT      NOT NULL,
    `status`     VARCHAR(45) NOT NULL,
    `createdAt`  DATETIME    NULL DEFAULT NULL,
    `createdBy`  BIGINT      NULL DEFAULT NULL,
    `updatedAt`  DATETIME    NULL DEFAULT NULL,
    `updatedBy`  BIGINT      NULL DEFAULT NULL,
    INDEX `fk_course_material_material_idx` (`materialId` ASC) VISIBLE,
    INDEX `fk_course_material_course_idx` (`courseId` ASC) VISIBLE,
    PRIMARY KEY (`id`),
    INDEX `fk_course_material_user_createdBy_idx` (`createdBy` ASC) VISIBLE,
    INDEX `fk_course_material_user_updatedBy_idx` (`updatedBy` ASC) VISIBLE,
    CONSTRAINT `fk_course_material_course`
        FOREIGN KEY (`courseId`)
            REFERENCES `teachsync`.`course` (`id`),
    CONSTRAINT `fk_course_material_material`
        FOREIGN KEY (`materialId`)
            REFERENCES `teachsync`.`material` (`id`),
    CONSTRAINT `fk_course_material_user_createdBy`
        FOREIGN KEY (`createdBy`)
            REFERENCES `teachsync`.`user` (`id`),
    CONSTRAINT `fk_course_material_user_updatedBy`
        FOREIGN KEY (`updatedBy`)
            REFERENCES `teachsync`.`user` (`id`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teachsync`.`test`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teachsync`.`test`;

CREATE TABLE IF NOT EXISTS `teachsync`.`test`
(
    `id`          BIGINT      NOT NULL AUTO_INCREMENT,
    `courseId`    BIGINT      NULL DEFAULT NULL,
    `testName`    VARCHAR(45) NOT NULL,
    `testType`    VARCHAR(45) NOT NULL COMMENT '15_MIN, MID_TERM, FINAL, ...',
    `testImg`     LONGTEXT    NULL DEFAULT NULL COMMENT 'Link to image',
    `testDesc`    LONGTEXT    NULL DEFAULT NULL,
    `timeLimit`   INT         NOT NULL COMMENT 'Count in MINUTES',
    `numQuestion` INT         NOT NULL,
    `minScore`    FLOAT       NOT NULL COMMENT 'Min score to pass',
    `testWeight`  INT         NOT NULL COMMENT 'Affect calculating final score',
    `totalScore`  FLOAT       NULL DEFAULT NULL,
    `status`      VARCHAR(45) NOT NULL,
    `createdAt`   DATETIME    NULL DEFAULT NULL,
    `createdBy`   BIGINT      NULL DEFAULT NULL,
    `updatedAt`   DATETIME    NULL DEFAULT NULL,
    `updatedBy`   BIGINT      NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_test_user_createdBy_idx` (`createdBy` ASC) VISIBLE,
    INDEX `fk_test_user_updatedBy_idx` (`updatedBy` ASC) VISIBLE,
    INDEX `fk_test_course_idx` (`courseId` ASC) VISIBLE,
    CONSTRAINT `fk_test_course`
        FOREIGN KEY (`courseId`)
            REFERENCES `teachsync`.`course` (`id`),
    CONSTRAINT `fk_test_user_createdBy`
        FOREIGN KEY (`createdBy`)
            REFERENCES `teachsync`.`user` (`id`),
    CONSTRAINT `fk_test_user_updatedBy`
        FOREIGN KEY (`updatedBy`)
            REFERENCES `teachsync`.`user` (`id`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teachsync`.`question`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teachsync`.`question`;

CREATE TABLE IF NOT EXISTS `teachsync`.`question`
(
    `id`             BIGINT       NOT NULL AUTO_INCREMENT,
    `testId`         BIGINT       NOT NULL,
    `questionType`   VARCHAR(255) NOT NULL COMMENT 'MULTIPLE, ESSAY, ...',
    `questionDesc`   LONGTEXT     NOT NULL COMMENT 'VD: Last night I think I ___ a ghost when I was going to the bathroom.',
    `questionPrompt` VARCHAR(45)  NOT NULL COMMENT 'VD: Chọn câu trả lời đúng nhất để điền vào chỗ trống',
    `status`         VARCHAR(45)  NOT NULL,
    `createdAt`      DATETIME     NULL DEFAULT NULL,
    `createdBy`      BIGINT       NULL DEFAULT NULL,
    `updatedAt`      DATETIME     NULL DEFAULT NULL,
    `updatedBy`      BIGINT       NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_question_test_idx` (`testId` ASC) VISIBLE,
    INDEX `fk_question_user_createdBy_idx` (`createdBy` ASC) VISIBLE,
    INDEX `fk_question_user_updatedBy_idx` (`updatedBy` ASC) VISIBLE,
    CONSTRAINT `fk_question_test`
        FOREIGN KEY (`testId`)
            REFERENCES `teachsync`.`test` (`id`),
    CONSTRAINT `fk_question_user_createdBy`
        FOREIGN KEY (`createdBy`)
            REFERENCES `teachsync`.`user` (`id`),
    CONSTRAINT `fk_question_user_updatedBy`
        FOREIGN KEY (`updatedBy`)
            REFERENCES `teachsync`.`user` (`id`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teachsync`.`answer`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teachsync`.`answer`;

CREATE TABLE IF NOT EXISTS `teachsync`.`answer`
(
    `id`          BIGINT      NOT NULL AUTO_INCREMENT,
    `questionId`  BIGINT      NOT NULL,
    `answerDesc`  LONGTEXT    NOT NULL COMMENT 'VD: saw',
    `answerScore` FLOAT       NOT NULL COMMENT 'Chỉ ghi điểm nếu câu trả lời này isCorrect = true',
    `isCorrect`   BIT(1)      NOT NULL,
    `status`      VARCHAR(45) NOT NULL,
    `createdAt`   DATETIME    NULL DEFAULT NULL,
    `createdBy`   BIGINT      NULL DEFAULT NULL,
    `updatedAt`   DATETIME    NULL DEFAULT NULL,
    `updatedBy`   BIGINT      NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_answer_question_idx` (`questionId` ASC) VISIBLE,
    INDEX `fk_answer_user_createdBy_idx` (`createdBy` ASC) VISIBLE,
    INDEX `fk_answer_user_updatedBy_idx` (`updatedBy` ASC) VISIBLE,
    CONSTRAINT `fk_answer_question`
        FOREIGN KEY (`questionId`)
            REFERENCES `teachsync`.`question` (`id`),
    CONSTRAINT `fk_answer_user_createdBy`
        FOREIGN KEY (`createdBy`)
            REFERENCES `teachsync`.`user` (`id`),
    CONSTRAINT `fk_answer_user_updatedBy`
        FOREIGN KEY (`updatedBy`)
            REFERENCES `teachsync`.`user` (`id`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teachsync`.`semester`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teachsync`.`semester`;

CREATE TABLE IF NOT EXISTS `teachsync`.`semester`
(
    `id`            BIGINT      NOT NULL AUTO_INCREMENT,
    `semesterName`  VARCHAR(45) NOT NULL COMMENT 'VD: FALL 2023',
    `semesterAlias` VARCHAR(10) NOT NULL COMMENT 'VD: FA23',
    `semesterDesc`  LONGTEXT    NULL DEFAULT NULL,
    `semesterType`  VARCHAR(45) NOT NULL COMMENT 'SEASONAL, AD_HOC, ...',
    `startDate`     DATE        NOT NULL,
    `endDate`       DATE        NOT NULL,
    `status`        VARCHAR(45) NOT NULL,
    `createdAt`     DATETIME    NULL DEFAULT NULL,
    `createdBy`     BIGINT      NULL DEFAULT NULL,
    `updatedAt`     DATETIME    NULL DEFAULT NULL,
    `updatedBy`     BIGINT      NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_semester_user_createdBy_idx` (`createdBy` ASC) VISIBLE,
    INDEX `fk_semester_user_updatedBy_idx` (`updatedBy` ASC) VISIBLE,
    CONSTRAINT `fk_semester_user_createdBy`
        FOREIGN KEY (`createdBy`)
            REFERENCES `teachsync`.`user` (`id`),
    CONSTRAINT `fk_semester_user_updatedBy`
        FOREIGN KEY (`updatedBy`)
            REFERENCES `teachsync`.`user` (`id`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teachsync`.`certificate`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teachsync`.`certificate`;

CREATE TABLE IF NOT EXISTS `teachsync`.`certificate`
(
    `id`                 BIGINT      NOT NULL AUTO_INCREMENT,
    `courseId`           BIGINT      NOT NULL,
    `certificateName`    VARCHAR(45) NOT NULL,
    `certificateDesc`    LONGTEXT    NULL DEFAULT NULL,
    `certificateImg`     LONGTEXT    NULL DEFAULT NULL,
    `certificateLink`    LONGTEXT    NULL DEFAULT NULL COMMENT 'Chứa link tới file',
    `certificateContent` MEDIUMBLOB  NULL DEFAULT NULL COMMENT 'Chứa file trong DB',
    `status`             VARCHAR(45) NOT NULL,
    `createdAt`          DATETIME    NULL DEFAULT NULL,
    `createdBy`          BIGINT      NULL DEFAULT NULL,
    `updatedAt`          DATETIME    NULL DEFAULT NULL,
    `updatedBy`          BIGINT      NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_certificate_user_createdBy_idx` (`createdBy` ASC) VISIBLE,
    INDEX `fk_certificate_user_updatedBy_idx` (`updatedBy` ASC) VISIBLE,
    INDEX `fk_certificate_course_idx` (`courseId` ASC) VISIBLE,
    CONSTRAINT `fk_certificate_course`
        FOREIGN KEY (`courseId`)
            REFERENCES `teachsync`.`course` (`id`),
    CONSTRAINT `fk_certificate_user_createdBy`
        FOREIGN KEY (`createdBy`)
            REFERENCES `teachsync`.`user` (`id`),
    CONSTRAINT `fk_certificate_user_updatedBy`
        FOREIGN KEY (`updatedBy`)
            REFERENCES `teachsync`.`user` (`id`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teachsync`.`certificate_issue`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teachsync`.`certificate_issue`;

CREATE TABLE IF NOT EXISTS `teachsync`.`certificate_issue`
(
    `id`            BIGINT      NOT NULL,
    `receiverId`    BIGINT      NOT NULL COMMENT 'Ai nhận. User id',
    `certificateId` BIGINT      NOT NULL COMMENT 'Bằng nào',
    `semesterId`    BIGINT      NOT NULL COMMENT 'Cho kì học nào',
    `signatoryId`   BIGINT      NOT NULL COMMENT 'Ai ký. User id',
    `presenterId`   BIGINT      NOT NULL COMMENT 'Ai trao bằng. User id',
    `issuedAt`      DATETIME    NULL DEFAULT NULL COMMENT 'Khi nào được trao (Có thể khác thời gian createdAt (thời gian nhập DB))',
    `printedAt`     DATETIME    NULL DEFAULT NULL COMMENT 'Khi nào in ra',
    `status`        VARCHAR(45) NOT NULL,
    `createdAt`     DATETIME    NULL DEFAULT NULL,
    `createdBy`     BIGINT      NULL DEFAULT NULL,
    `updatedAt`     DATETIME    NULL DEFAULT NULL,
    `updatedBy`     BIGINT      NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_certificate_issue_certificate_idx` (`certificateId` ASC) VISIBLE,
    INDEX `fk_certificate_issue_user_receiverId_idx` (`receiverId` ASC) VISIBLE,
    INDEX `fk_certificate_issue_semester_idx` (`semesterId` ASC) INVISIBLE,
    INDEX `fk_certificate_issue_user_createdBy_idx` (`createdBy` ASC) VISIBLE,
    INDEX `fk_certificate_issue_user_updatedBy_idx` (`updatedBy` ASC) VISIBLE,
    INDEX `fk_certificate_issue_user_signatoryId_idx` (`signatoryId` ASC) VISIBLE,
    INDEX `fk_certificate_issue_user_presenterId_idx` (`presenterId` ASC) VISIBLE,
    CONSTRAINT `fk_certificate_issue_user_receiverId`
        FOREIGN KEY (`receiverId`)
            REFERENCES `teachsync`.`user` (`id`),
    CONSTRAINT `fk_certificate_issue_certificate`
        FOREIGN KEY (`certificateId`)
            REFERENCES `teachsync`.`certificate` (`id`),
    CONSTRAINT `fk_certificate_issue_semester`
        FOREIGN KEY (`semesterId`)
            REFERENCES `teachsync`.`semester` (`id`),
    CONSTRAINT `fk_certificate_issue_user_presenterId`
        FOREIGN KEY (`presenterId`)
            REFERENCES `teachsync`.`user` (`id`),
    CONSTRAINT `fk_certificate_issue_user_createdBy`
        FOREIGN KEY (`createdBy`)
            REFERENCES `teachsync`.`user` (`id`),
    CONSTRAINT `fk_certificate_issue_user_updatedBy`
        FOREIGN KEY (`updatedBy`)
            REFERENCES `teachsync`.`user` (`id`),
    CONSTRAINT `fk_certificate_issue_user_signatoryId`
        FOREIGN KEY (`signatoryId`)
            REFERENCES `teachsync`.`user` (`id`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teachsync`.`course_semester`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teachsync`.`course_semester`;

CREATE TABLE IF NOT EXISTS `teachsync`.`course_semester`
(
    `id`         BIGINT      NOT NULL AUTO_INCREMENT,
    `courseId`   BIGINT      NOT NULL COMMENT 'Môn gì',
    `semesterId` BIGINT      NOT NULL COMMENT 'Khi nào',
    `centerId`   BIGINT      NOT NULL COMMENT 'Ở đâu',
    `status`     VARCHAR(45) NOT NULL,
    `createdAt`  DATETIME    NULL DEFAULT NULL,
    `createdBy`  BIGINT      NULL DEFAULT NULL,
    `updatedAt`  DATETIME    NULL DEFAULT NULL,
    `updatedBy`  BIGINT      NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_course_semester_semester_idx` (`semesterId` ASC) VISIBLE,
    INDEX `fk_course_semester_center_idx` (`centerId` ASC) VISIBLE,
    INDEX `fk_course_semester_course_idx` (`courseId` ASC) VISIBLE,
    INDEX `fk_course_semester_user_createdBy_idx` (`createdBy` ASC) VISIBLE,
    INDEX `fk_course_semester_user_updatedBy_idx` (`updatedBy` ASC) VISIBLE,
    CONSTRAINT `fk_course_semester_center`
        FOREIGN KEY (`centerId`)
            REFERENCES `teachsync`.`center` (`id`),
    CONSTRAINT `fk_course_semester_course`
        FOREIGN KEY (`courseId`)
            REFERENCES `teachsync`.`course` (`id`),
    CONSTRAINT `fk_course_semester_semester`
        FOREIGN KEY (`semesterId`)
            REFERENCES `teachsync`.`semester` (`id`),
    CONSTRAINT `fk_course_semester_user_createdBy`
        FOREIGN KEY (`createdBy`)
            REFERENCES `teachsync`.`user` (`id`),
    CONSTRAINT `fk_course_semester_user_updatedBy`
        FOREIGN KEY (`updatedBy`)
            REFERENCES `teachsync`.`user` (`id`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teachsync`.`clazz`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teachsync`.`clazz`;

CREATE TABLE IF NOT EXISTS `teachsync`.`clazz`
(
    `id`               BIGINT      NOT NULL AUTO_INCREMENT,
    `courseSemesterId` BIGINT      NOT NULL COMMENT 'Tức Môn gì, khi nào, ở đâu gói gọn làm 1',
    `staffId`          BIGINT      NOT NULL COMMENT 'Ai dạy (Giáo viên chính thức) (staffRole cần = TEACHER)',
    `clazzName`        VARCHAR(45) NOT NULL,
    `clazzDesc`        LONGTEXT    NULL DEFAULT NULL,
    `clazzSize`        INT         NOT NULL COMMENT 'Số học sinh tối đa',
    `status`           VARCHAR(45) NOT NULL,
    `createdAt`        DATETIME    NULL DEFAULT NULL,
    `createdBy`        BIGINT      NULL DEFAULT NULL,
    `updatedAt`        DATETIME    NULL DEFAULT NULL,
    `updatedBy`        BIGINT      NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_clazz_course_semester_idx` (`courseSemesterId` ASC) VISIBLE,
    INDEX `fk_clazz_staff_idx` (`staffId` ASC) VISIBLE,
    INDEX `fk_clazz_user_createdBy_idx` (`createdBy` ASC) VISIBLE,
    INDEX `fk_clazz_user_updatedBy_idx` (`updatedBy` ASC) VISIBLE,
    CONSTRAINT `fk_clazz_course_semester`
        FOREIGN KEY (`courseSemesterId`)
            REFERENCES `teachsync`.`course_semester` (`id`),
    CONSTRAINT `fk_clazz_staff`
        FOREIGN KEY (`staffId`)
            REFERENCES `teachsync`.`staff` (`id`),
    CONSTRAINT `fk_clazz_user_createdBy`
        FOREIGN KEY (`createdBy`)
            REFERENCES `teachsync`.`user` (`id`),
    CONSTRAINT `fk_clazz_user_updatedBy`
        FOREIGN KEY (`updatedBy`)
            REFERENCES `teachsync`.`user` (`id`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teachsync`.`clazz_member`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teachsync`.`clazz_member`;

CREATE TABLE IF NOT EXISTS `teachsync`.`clazz_member`
(
    `id`        BIGINT      NOT NULL AUTO_INCREMENT,
    `clazzId`   BIGINT      NOT NULL,
    `userId`    BIGINT      NOT NULL,
    `score`     FLOAT       NULL DEFAULT NULL COMMENT 'Ghi kết quả sau khi xong môn. Mặc định null. Điểm tổng (được tính từ table MemberTestRecord)',
    `attendant` FLOAT       NULL DEFAULT NULL COMMENT 'Ghi kết quả sau khi xong môn. Mặc định null. Số % có mặt (được tính từ table Attendant)',
    `isPassed`  BIT(1)      NULL DEFAULT NULL COMMENT 'Ghi kết quả sau khi xong môn. Mặc định null',
    `status`    VARCHAR(45) NOT NULL,
    `createdAt` DATETIME    NULL DEFAULT NULL,
    `createdBy` BIGINT      NULL DEFAULT NULL,
    `updatedAt` DATETIME    NULL DEFAULT NULL,
    `updatedBy` BIGINT      NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_clazz_member_clazz_idx` (`clazzId` ASC) VISIBLE,
    INDEX `fk_clazz_member_user_idx` (`userId` ASC) VISIBLE,
    INDEX `fk_clazz_member_user_createdBy_idx` (`createdBy` ASC) VISIBLE,
    INDEX `fk_clazz_member_user_updatedBy_idx` (`updatedBy` ASC) VISIBLE,
    CONSTRAINT `fk_clazz_member_clazz`
        FOREIGN KEY (`clazzId`)
            REFERENCES `teachsync`.`clazz` (`id`),
    CONSTRAINT `fk_clazz_member_user`
        FOREIGN KEY (`userId`)
            REFERENCES `teachsync`.`user` (`id`),
    CONSTRAINT `fk_clazz_member_user_createdBy`
        FOREIGN KEY (`createdBy`)
            REFERENCES `teachsync`.`user` (`id`),
    CONSTRAINT `fk_clazz_member_user_updatedBy`
        FOREIGN KEY (`updatedBy`)
            REFERENCES `teachsync`.`user` (`id`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teachsync`.`clazz_schedule`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teachsync`.`clazz_schedule`;

CREATE TABLE IF NOT EXISTS `teachsync`.`clazz_schedule`
(
    `id`           BIGINT      NOT NULL AUTO_INCREMENT,
    `clazzId`      BIGINT      NOT NULL,
    `roomId`       BIGINT      NOT NULL COMMENT 'Phòng mặc định của lớp',
    `scheduleType` VARCHAR(45) NOT NULL COMMENT '2_4_6, 3_5_7, T7_CN, CUSTOM, ...',
    `startDate`    DATE        NOT NULL,
    `endDate`      DATE        NOT NULL,
    `slot`         INT         NULL DEFAULT NULL,
    `sessionStart` TIME        NOT NULL COMMENT 'Giờ bắt đầu (Auto tính từ slot)',
    `sessionEnd`   TIME        NOT NULL COMMENT 'Giờ kết thức (Auto tính từ slot)',
    `status`       VARCHAR(45) NOT NULL,
    `createdAt`    DATETIME    NULL DEFAULT NULL,
    `createdBy`    BIGINT      NULL DEFAULT NULL,
    `updatedAt`    DATETIME    NULL DEFAULT NULL,
    `updatedBy`    BIGINT      NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_clazz_schedule_clazz_idx` (`clazzId` ASC) INVISIBLE,
    INDEX `fk_clazz_schedule_room_idx` (`roomId` ASC) VISIBLE,
    INDEX `fk_clazz_schedule_user_createdBy_idx` (`createdBy` ASC) VISIBLE,
    INDEX `fk_clazz_schedule_user_updatedBy_idx` (`updatedBy` ASC) VISIBLE,
    CONSTRAINT `fk_clazz_schedule_room`
        FOREIGN KEY (`roomId`)
            REFERENCES `teachsync`.`room` (`id`),
    CONSTRAINT `fk_clazz_schedule_clazz`
        FOREIGN KEY (`clazzId`)
            REFERENCES `teachsync`.`clazz` (`id`),
    CONSTRAINT `fk_clazz_schedule_user_createdBy`
        FOREIGN KEY (`createdBy`)
            REFERENCES `teachsync`.`user` (`id`),
    CONSTRAINT `fk_clazz_schedule_user_updatedBy`
        FOREIGN KEY (`updatedBy`)
            REFERENCES `teachsync`.`user` (`id`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teachsync`.`session`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teachsync`.`session`;

CREATE TABLE IF NOT EXISTS `teachsync`.`session`
(
    `id`           BIGINT      NOT NULL AUTO_INCREMENT,
    `roomId`       BIGINT      NOT NULL COMMENT 'Phòng học thực tế (Mặc định theo ClazzSchedule. Sẽ update nếu chuyển phòng)',
    `scheduleId`   BIGINT      NOT NULL,
    `staffId`      BIGINT      NOT NULL COMMENT 'Người dạy thực tế (Mặc định theo Clazz. Sẽ update nếu dạy thay)',
    `slot`         INT         NULL DEFAULT NULL COMMENT 'Slot thực tế (Mặc định theo ClazzSchedule. Sẽ update nếu đổi lịch)',
    `sessionStart` DATETIME    NOT NULL COMMENT 'Ngày và Giờ bắt đầu thực tế (Mặc định theo ClazzSchedule. Sẽ update nếu đổi lịch)',
    `sessionEnd`   DATETIME    NOT NULL COMMENT 'Ngày và Giờ kết thúc thực tế (Mặc định theo ClazzSchedule. Sẽ update nếu đổi lịch)',
    `status`       VARCHAR(45) NOT NULL,
    `createdAt`    DATETIME    NULL DEFAULT NULL,
    `createdBy`    BIGINT      NULL DEFAULT NULL,
    `updatedAt`    DATETIME    NULL DEFAULT NULL,
    `updatedBy`    BIGINT      NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_session_room_idx` (`roomId` ASC) VISIBLE,
    INDEX `fk_session_schedule_idx` (`scheduleId` ASC) VISIBLE,
    INDEX `fk_session_user_createdBy_idx` (`createdBy` ASC) VISIBLE,
    INDEX `fk_session_user_updatedBy_idx` (`updatedBy` ASC) VISIBLE,
    INDEX `fk_session_center_staff_idx` (`staffId` ASC) VISIBLE,
    CONSTRAINT `fk_session_room`
        FOREIGN KEY (`roomId`)
            REFERENCES `teachsync`.`room` (`id`),
    CONSTRAINT `fk_session_schedule`
        FOREIGN KEY (`scheduleId`)
            REFERENCES `teachsync`.`clazz_schedule` (`id`),
    CONSTRAINT `fk_session_center_staff`
        FOREIGN KEY (`staffId`)
            REFERENCES `teachsync`.`staff` (`id`),
    CONSTRAINT `fk_session_user_createdBy`
        FOREIGN KEY (`createdBy`)
            REFERENCES `teachsync`.`user` (`id`),
    CONSTRAINT `fk_session_user_updatedBy`
        FOREIGN KEY (`updatedBy`)
            REFERENCES `teachsync`.`user` (`id`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teachsync`.`attendant`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teachsync`.`attendant`;

CREATE TABLE IF NOT EXISTS `teachsync`.`attendant`
(
    `id`        BIGINT      NOT NULL AUTO_INCREMENT,
    `sessionId` BIGINT      NOT NULL COMMENT 'Tiết nào',
    `memberId`  BIGINT      NOT NULL COMMENT 'Ai',
    `isPresent` BIT(1)      NOT NULL COMMENT 'Có mặt / vắng mặt (true/false)',
    `status`    VARCHAR(45) NOT NULL,
    `createdAt` DATETIME    NULL DEFAULT NULL,
    `createdBy` BIGINT      NULL DEFAULT NULL,
    `updatedAt` DATETIME    NULL DEFAULT NULL,
    `updatedBy` BIGINT      NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_attendant_clazz_member_idx` (`memberId` ASC) VISIBLE,
    INDEX `fk_attendant_session_idx` (`sessionId` ASC) VISIBLE,
    INDEX `fk_attendant_user_createdBy_idx` (`createdBy` ASC) VISIBLE,
    INDEX `fk_attendant_user_updatedBy_idx` (`updatedBy` ASC) VISIBLE,
    CONSTRAINT `fk_attendant_session`
        FOREIGN KEY (`sessionId`)
            REFERENCES `teachsync`.`session` (`id`),
    CONSTRAINT `fk_attendant_clazz_member`
        FOREIGN KEY (`memberId`)
            REFERENCES `teachsync`.`clazz_member` (`id`),
    CONSTRAINT `fk_attendant_user_createdBy`
        FOREIGN KEY (`createdBy`)
            REFERENCES `teachsync`.`user` (`id`),
    CONSTRAINT `fk_attendant_user_updatedBy`
        FOREIGN KEY (`updatedBy`)
            REFERENCES `teachsync`.`user` (`id`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teachsync`.`homework`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teachsync`.`homework`;

CREATE TABLE IF NOT EXISTS `teachsync`.`homework`
(
    `id`              BIGINT      NOT NULL AUTO_INCREMENT,
    `clazzId`         BIGINT      NOT NULL,
    `homeworkName`    VARCHAR(45) NOT NULL COMMENT 'Bài dịch số 2',
    `homeworkDesc`    LONGTEXT    NULL DEFAULT NULL COMMENT 'Dịch đoạn văn trong file Word đính kèm. (Nộp bài qua file dưới dạng Word, không được dùng file ảnh)',
    `homeworkDoc`     LONGTEXT    NULL DEFAULT NULL COMMENT 'Link to file attachment',
    `homeworkContent` MEDIUMBLOB  NULL DEFAULT NULL COMMENT 'File attachment store in DB',
    `openAt`          DATETIME    NULL DEFAULT NULL COMMENT 'Thời gian mở xem bài và nộp bài',
    `deadline`        DATETIME    NULL DEFAULT NULL COMMENT 'Hạn chót nộp bài',
    `status`          VARCHAR(45) NOT NULL,
    `createdAt`       DATETIME    NULL DEFAULT NULL,
    `createdBy`       BIGINT      NULL DEFAULT NULL,
    `updatedAt`       DATETIME    NULL DEFAULT NULL,
    `updatedBy`       BIGINT      NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_homework_clazz_idx` (`clazzId` ASC) VISIBLE,
    INDEX `fk_homework_user_createdBy_idx` (`createdBy` ASC) VISIBLE,
    INDEX `fk_homework_user_updatedBy_idx` (`updatedBy` ASC) VISIBLE,
    CONSTRAINT `fk_homework_clazz`
        FOREIGN KEY (`clazzId`)
            REFERENCES `teachsync`.`clazz` (`id`),
    CONSTRAINT `fk_homework_user_createdBy`
        FOREIGN KEY (`createdBy`)
            REFERENCES `teachsync`.`user` (`id`),
    CONSTRAINT `fk_homework_user_updatedBy`
        FOREIGN KEY (`updatedBy`)
            REFERENCES `teachsync`.`user` (`id`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teachsync`.`member_homework_record`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teachsync`.`member_homework_record`;

CREATE TABLE IF NOT EXISTS `teachsync`.`member_homework_record`
(
    `id`             BIGINT      NOT NULL AUTO_INCREMENT,
    `memberId`       BIGINT      NOT NULL COMMENT 'Ai làm',
    `homeworkId`     BIGINT      NOT NULL COMMENT 'Bài gì',
    `submission`     MEDIUMBLOB  NULL DEFAULT NULL COMMENT 'File store in DB',
    `submissionLink` LONGTEXT    NULL DEFAULT NULL COMMENT 'Link to file',
    `score`          FLOAT       NULL DEFAULT NULL COMMENT 'Nhiêu điểm. (Max = 10.00) (Chỉ được update = teacher)',
    `status`         VARCHAR(45) NOT NULL,
    `createdAt`      DATETIME    NULL DEFAULT NULL,
    `createdBy`      BIGINT      NULL DEFAULT NULL,
    `updatedAt`      DATETIME    NULL DEFAULT NULL,
    `updatedBy`      BIGINT      NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_member_homework_record_clazz_member_idx` (`memberId` ASC) VISIBLE,
    INDEX `fk_member_homework_record_homework_idx` (`homeworkId` ASC) VISIBLE,
    INDEX `fk_member_homework_record_user_createdBy_idx` (`createdBy` ASC) VISIBLE,
    INDEX `fk_member_homework_record_user_updatedBy_idx` (`updatedBy` ASC) VISIBLE,
    CONSTRAINT `fk_member_homework_record_clazz_member`
        FOREIGN KEY (`memberId`)
            REFERENCES `teachsync`.`clazz_member` (`id`),
    CONSTRAINT `fk_member_homework_record_homework`
        FOREIGN KEY (`homeworkId`)
            REFERENCES `teachsync`.`homework` (`id`),
    CONSTRAINT `fk_member_homework_record_user_createdBy`
        FOREIGN KEY (`createdBy`)
            REFERENCES `teachsync`.`user` (`id`),
    CONSTRAINT `fk_member_homework_record_user_updatedBy`
        FOREIGN KEY (`updatedBy`)
            REFERENCES `teachsync`.`user` (`id`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teachsync`.`clazz_test`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teachsync`.`clazz_test`;

CREATE TABLE IF NOT EXISTS `teachsync`.`clazz_test`
(
    `id`        BIGINT      NOT NULL AUTO_INCREMENT,
    `clazzId`   BIGINT      NOT NULL,
    `testId`    BIGINT      NOT NULL,
    `openFrom`  DATETIME    NULL DEFAULT NULL COMMENT 'Thời gian mở làm bài (Mặc định null. Giáo viên update ngày giờ cụ thể theo giáo trình)',
    `openTo`    DATETIME    NULL DEFAULT NULL COMMENT 'Thời gian đóng làm bài (Mặc định null. Giáo viên update ngày giờ cụ thể theo giáo trình)',
    `status`    VARCHAR(45) NOT NULL,
    `createdAt` DATETIME    NULL DEFAULT NULL,
    `createdBy` BIGINT      NULL DEFAULT NULL,
    `updatedAt` DATETIME    NULL DEFAULT NULL,
    `updatedBy` BIGINT      NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_clazz_test_user_updatedBy_idx` (`updatedBy` ASC) VISIBLE,
    INDEX `fk_clazz_test_user_createdBy_idx` (`createdBy` ASC) VISIBLE,
    INDEX `fk_clazz_test_test_idx` (`testId` ASC) VISIBLE,
    INDEX `fk_clazz_test_clazz_idx` (`clazzId` ASC) VISIBLE,
    CONSTRAINT `fk_clazz_test_clazz`
        FOREIGN KEY (`clazzId`)
            REFERENCES `teachsync`.`clazz` (`id`),
    CONSTRAINT `fk_clazz_test_test`
        FOREIGN KEY (`testId`)
            REFERENCES `teachsync`.`test` (`id`),
    CONSTRAINT `fk_clazz_test_user_createdBy`
        FOREIGN KEY (`createdBy`)
            REFERENCES `teachsync`.`user` (`id`),
    CONSTRAINT `fk_clazz_test_user_updatedBy`
        FOREIGN KEY (`updatedBy`)
            REFERENCES `teachsync`.`user` (`id`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teachsync`.`member_test_record`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teachsync`.`member_test_record`;

CREATE TABLE IF NOT EXISTS `teachsync`.`member_test_record`
(
    `id`          BIGINT      NOT NULL AUTO_INCREMENT,
    `memberId`    BIGINT      NOT NULL COMMENT 'Ai làm',
    `clazzTestId` BIGINT      NOT NULL COMMENT 'Bài test nào (!= với Test, ClazzTest có ngày giờ mở đóng bài cụ thể cho từng lớp)',
    `score`       DOUBLE      NULL DEFAULT NULL COMMENT 'Bao nhiêu điểm (max = 10.00) (Auto tính = code. Giáo viên update nếu bài test type ESSAY)',
    `status`      VARCHAR(45) NOT NULL,
    `createdAt`   DATETIME    NULL DEFAULT NULL,
    `createdBy`   BIGINT      NULL DEFAULT NULL,
    `updatedAt`   DATETIME    NULL DEFAULT NULL,
    `updatedBy`   BIGINT      NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_member_test_record_clazz_member_idx` (`memberId` ASC) VISIBLE,
    INDEX `fk_member_test_record_clazz_test_idx` (`clazzTestId` ASC) VISIBLE,
    INDEX `fk_member_test_record_user_createdBy_idx` (`createdBy` ASC) VISIBLE,
    INDEX `fk_member_test_record_user_updatedBy_idx` (`updatedBy` ASC) VISIBLE,
    CONSTRAINT `fk_member_test_record_clazz_member`
        FOREIGN KEY (`memberId`)
            REFERENCES `teachsync`.`clazz_member` (`id`),
    CONSTRAINT `fk_member_test_record_clazz_test`
        FOREIGN KEY (`clazzTestId`)
            REFERENCES `teachsync`.`clazz_test` (`id`),
    CONSTRAINT `fk_member_test_record_user_createdBy`
        FOREIGN KEY (`createdBy`)
            REFERENCES `teachsync`.`user` (`id`),
    CONSTRAINT `fk_member_test_record_user_updatedBy`
        FOREIGN KEY (`updatedBy`)
            REFERENCES `teachsync`.`user` (`id`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teachsync`.`test_record`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teachsync`.`test_record`;

CREATE TABLE IF NOT EXISTS `teachsync`.`test_record`
(
    `id`                 BIGINT      NOT NULL AUTO_INCREMENT,
    `memberTestRecordId` BIGINT      NOT NULL COMMENT 'Ai làm, bài test nào',
    `answerId`           BIGINT      NOT NULL COMMENT 'Câu trả lời chọn (Câu hỏi MULTIPLE)',
    `answerTxt`          LONGTEXT    NULL DEFAULT NULL COMMENT 'Câu trả lời ghi ra (Câu hỏi ESSAY. Giáo viên cần chấm)',
    `score`              FLOAT       NULL DEFAULT NULL COMMENT 'Điểm đặt được (Auto tính = code. ESSAY thì giáo viên update)',
    `status`             VARCHAR(45) NOT NULL,
    `createdAt`          DATETIME    NULL DEFAULT NULL,
    `createdBy`          BIGINT      NULL DEFAULT NULL,
    `updatedAt`          DATETIME    NULL DEFAULT NULL,
    `updatedBy`          BIGINT      NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_test_record_answer_idx` (`answerId` ASC) VISIBLE,
    INDEX `fk_test_record_member_test_record` (`memberTestRecordId` ASC) VISIBLE,
    INDEX `fk_test_record_user_createdBy_idx` (`createdBy` ASC) VISIBLE,
    INDEX `fk_test_record_user_updatedBy_idx` (`updatedBy` ASC) VISIBLE,
    CONSTRAINT `fk_test_record_answer`
        FOREIGN KEY (`answerId`)
            REFERENCES `teachsync`.`answer` (`id`),
    CONSTRAINT `fk_test_record_member_test_record`
        FOREIGN KEY (`memberTestRecordId`)
            REFERENCES `teachsync`.`member_test_record` (`id`),
    CONSTRAINT `fk_test_record_user_createdBy`
        FOREIGN KEY (`createdBy`)
            REFERENCES `teachsync`.`user` (`id`),
    CONSTRAINT `fk_test_record_user_updatedBy`
        FOREIGN KEY (`updatedBy`)
            REFERENCES `teachsync`.`user` (`id`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teachsync`.`request`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teachsync`.`request`;

CREATE TABLE IF NOT EXISTS `teachsync`.`request`
(
    `id`             BIGINT      NOT NULL AUTO_INCREMENT,
    `requesterId`    BIGINT      NOT NULL COMMENT 'Ai xin',
    `requestName`    VARCHAR(45) NOT NULL,
    `requestDesc`    LONGTEXT    NOT NULL,
    `requestType`    VARCHAR(45) NOT NULL COMMENT 'CHANGE_CLAZZ, ENROLL, ABSENT, ...',
    `clazzId`        BIGINT      NULL DEFAULT NULL,
    `requestContent` MEDIUMBLOB  NULL DEFAULT NULL,
    `contentLink`    LONGTEXT    NULL DEFAULT NULL,
    `resolverId`     BIGINT      NULL DEFAULT NULL COMMENT 'Ai duyệt/xử lý',
    `status`         VARCHAR(45) NOT NULL,
    `createdAt`      DATETIME    NULL DEFAULT NULL,
    `createdBy`      BIGINT      NULL DEFAULT NULL,
    `updatedAt`      DATETIME    NULL DEFAULT NULL,
    `updatedBy`      BIGINT      NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_request_user_requesterId_idx` (`requesterId` ASC) VISIBLE,
    INDEX `fk_request_user_resolverId_idx` (`resolverId` ASC) VISIBLE,
    INDEX `fk_request_user_createdBy_idx` (`createdBy` ASC) VISIBLE,
    INDEX `fk_request_user_updatedBy_idx` (`updatedBy` ASC) VISIBLE,
    INDEX `fk_request_clazz_idx` (`clazzId` ASC) VISIBLE,
    CONSTRAINT `fk_request_clazz`
        FOREIGN KEY (`clazzId`)
            REFERENCES `teachsync`.`clazz` (`id`),
    CONSTRAINT `fk_request_user_requesterId`
        FOREIGN KEY (`requesterId`)
            REFERENCES `teachsync`.`user` (`id`),
    CONSTRAINT `fk_request_user_resolverId`
        FOREIGN KEY (`resolverId`)
            REFERENCES `teachsync`.`user` (`id`),
    CONSTRAINT `fk_request_user_createdBy`
        FOREIGN KEY (`createdBy`)
            REFERENCES `teachsync`.`user` (`id`),
    CONSTRAINT `fk_request_user_updatedBy`
        FOREIGN KEY (`updatedBy`)
            REFERENCES `teachsync`.`user` (`id`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teachsync`.`payment`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teachsync`.`payment`;

CREATE TABLE IF NOT EXISTS `teachsync`.`payment`
(
    `id`             BIGINT      NOT NULL AUTO_INCREMENT,
    `payerId`        BIGINT      NOT NULL,
    `requestId`      BIGINT      NOT NULL,
    `paymentType`    VARCHAR(45) NOT NULL,
    `paymentAmount`  DOUBLE      NOT NULL,
    `paymentAt`      DATETIME    NOT NULL,
    `paymentDoc`     MEDIUMBLOB  NULL DEFAULT NULL,
    `paymentDocLink` LONGTEXT    NULL DEFAULT NULL,
    `verifierId`     BIGINT      NOT NULL,
    `status`         VARCHAR(45) NOT NULL,
    `createdAt`      DATETIME    NULL DEFAULT NULL,
    `createdBy`      BIGINT      NULL DEFAULT NULL,
    `updatedAt`      DATETIME    NULL DEFAULT NULL,
    `updatedBy`      BIGINT      NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_payment_request_idx` (`requestId` ASC) VISIBLE,
    INDEX `fk_payment_user_payerId_idx` (`payerId` ASC) VISIBLE,
    INDEX `fk_payment_user_verifierId_idx` (`verifierId` ASC) VISIBLE,
    INDEX `fk_payment_user_createdBy_idx` (`createdBy` ASC) VISIBLE,
    INDEX `fk_payment_user_updatedBy_idx` (`updatedBy` ASC) VISIBLE,
    CONSTRAINT `fk_payment_request`
        FOREIGN KEY (`requestId`)
            REFERENCES `teachsync`.`request` (`id`),
    CONSTRAINT `fk_payment_user_payerId`
        FOREIGN KEY (`payerId`)
            REFERENCES `teachsync`.`user` (`id`),
    CONSTRAINT `fk_payment_user_verifierId`
        FOREIGN KEY (`verifierId`)
            REFERENCES `teachsync`.`user` (`id`),
    CONSTRAINT `fk_payment_user_createdBy`
        FOREIGN KEY (`createdBy`)
            REFERENCES `teachsync`.`user` (`id`),
    CONSTRAINT `fk_payment_user_updatedBy`
        FOREIGN KEY (`updatedBy`)
            REFERENCES `teachsync`.`user` (`id`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teachsync`.`wallet`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teachsync`.`wallet`;

CREATE TABLE IF NOT EXISTS `teachsync`.`wallet`
(
    `id`             BIGINT       NOT NULL AUTO_INCREMENT,
    `userId`         BIGINT       NOT NULL,
    `paymentId`      BIGINT       NULL DEFAULT NULL,
    `currentBalance` DOUBLE       NOT NULL COMMENT 'Auto calculate by code, save on query',
    `note`           VARCHAR(255) NULL DEFAULT NULL,
    `amountChange`   DOUBLE       NOT NULL,
    `changeAt`       DATETIME     NOT NULL,
    `status`         VARCHAR(45)  NOT NULL,
    `createdAt`      DATETIME     NULL DEFAULT NULL,
    `createdBy`      BIGINT       NULL DEFAULT NULL,
    `updatedAt`      DATETIME     NULL DEFAULT NULL,
    `updatedBy`      BIGINT       NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_wallet_payment_idx` (`paymentId` ASC) VISIBLE,
    INDEX `fk_wallet_user_idx` (`userId` ASC) VISIBLE,
    INDEX `fk_wallet_user_createdBy_idx` (`createdBy` ASC) VISIBLE,
    INDEX `fk_wallet_user_updatedBy_idx` (`updatedBy` ASC) VISIBLE,
    CONSTRAINT `fk_wallet_payment`
        FOREIGN KEY (`paymentId`)
            REFERENCES `teachsync`.`payment` (`id`),
    CONSTRAINT `fk_wallet_user`
        FOREIGN KEY (`userId`)
            REFERENCES `teachsync`.`user` (`id`),
    CONSTRAINT `fk_wallet_user_createdBy`
        FOREIGN KEY (`createdBy`)
            REFERENCES `teachsync`.`user` (`id`),
    CONSTRAINT `fk_wallet_user_updatedBy`
        FOREIGN KEY (`updatedBy`)
            REFERENCES `teachsync`.`user` (`id`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teachsync`.`news`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teachsync`.`news`;

CREATE TABLE IF NOT EXISTS `teachsync`.`news`
(
    `id`          BIGINT      NOT NULL AUTO_INCREMENT,
    `authorId`    BIGINT      NOT NULL,
    `newsTitle`   VARCHAR(45) NOT NULL,
    `newsContent` MEDIUMBLOB  NULL DEFAULT NULL,
    `newsLink`    LONGTEXT    NULL DEFAULT NULL,
    `newsDesc`    LONGTEXT    NULL DEFAULT NULL,
    `status`      VARCHAR(45) NOT NULL,
    `createdAt`   DATETIME    NULL DEFAULT NULL,
    `createdBy`   BIGINT      NULL DEFAULT NULL,
    `updatedAt`   DATETIME    NULL DEFAULT NULL,
    `updatedBy`   BIGINT      NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_news_user_authorId_idx` (`authorId` ASC) VISIBLE,
    INDEX `fk_news_user_createdBy_idx` (`createdBy` ASC) VISIBLE,
    INDEX `fk_news_user_updatedBy_idx` (`updatedBy` ASC) VISIBLE,
    CONSTRAINT `fk_news_user_authorId`
        FOREIGN KEY (`authorId`)
            REFERENCES `teachsync`.`user` (`id`),
    CONSTRAINT `fk_news_user_createdBy`
        FOREIGN KEY (`createdBy`)
            REFERENCES `teachsync`.`user` (`id`),
    CONSTRAINT `fk_news_user_updatedBy`
        FOREIGN KEY (`updatedBy`)
            REFERENCES `teachsync`.`user` (`id`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teachsync`.`recruitment_campaign`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teachsync`.`recruitment_campaign`;

CREATE TABLE IF NOT EXISTS `teachsync`.`recruitment_campaign`
(
    `id`           BIGINT      NOT NULL AUTO_INCREMENT,
    `centerId`     BIGINT      NOT NULL COMMENT 'Cơ sở nào tuyển',
    `campaignImg`  LONGTEXT    NOT NULL,
    `campaignName` VARCHAR(45) NOT NULL,
    `campaignDesc` LONGTEXT    NULL DEFAULT NULL,
    `position`     VARCHAR(45) NOT NULL COMMENT 'Tuyển chức vụ gì',
    `openingSlot`  INT         NOT NULL COMMENT 'Tuyển bao nhiêu người',
    `recruitFrom`  DATETIME    NOT NULL,
    `recruitTo`    DATETIME    NOT NULL,
    `status`       VARCHAR(45) NOT NULL,
    `createdAt`    DATETIME    NULL DEFAULT NULL,
    `createdBy`    BIGINT      NULL DEFAULT NULL,
    `updatedAt`    DATETIME    NULL DEFAULT NULL,
    `updatedBy`    BIGINT      NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_recruitment_campaign_center_idx` (`centerId` ASC) VISIBLE,
    INDEX `fk_recruitment_campaign_user_createdBy_idx` (`createdBy` ASC) VISIBLE,
    INDEX `fk_recruitment_campaign_user_updatedBy_idx` (`updatedBy` ASC) VISIBLE,
    CONSTRAINT `fk_campaign_center`
        FOREIGN KEY (`centerId`)
            REFERENCES `teachsync`.`center` (`id`),
    CONSTRAINT `fk_recruitment_campaign_user_createdBy`
        FOREIGN KEY (`createdBy`)
            REFERENCES `teachsync`.`user` (`id`),
    CONSTRAINT `fk_recruitment_campaign_user_updatedBy`
        FOREIGN KEY (`updatedBy`)
            REFERENCES `teachsync`.`user` (`id`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teachsync`.`campaign_application`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teachsync`.`campaign_application`;

CREATE TABLE IF NOT EXISTS `teachsync`.`campaign_application`
(
    `id`          BIGINT      NOT NULL AUTO_INCREMENT,
    `campaignId`  BIGINT      NOT NULL COMMENT 'Xin vào đâu, chức gì',
    `applicantId` BIGINT      NOT NULL COMMENT 'Ai xin',
    `appliedAt`   DATETIME    NOT NULL,
    `result`      VARCHAR(45) NOT NULL COMMENT 'PENDING, ACCEPTED, REJECTED, ...',
    `resultDate`  DATETIME    NULL DEFAULT NULL,
    `status`      VARCHAR(45) NOT NULL,
    `createdAt`   DATETIME    NULL DEFAULT NULL,
    `createdBy`   BIGINT      NULL DEFAULT NULL,
    `updatedAt`   DATETIME    NULL DEFAULT NULL,
    `updatedBy`   BIGINT      NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_campaign_application_user_createdBy_idx` (`createdBy` ASC) VISIBLE,
    INDEX `fk_campaign_application_user_updatedBy_idx` (`updatedBy` ASC) VISIBLE,
    INDEX `fk_application_campaign_idx` (`campaignId` ASC) VISIBLE,
    INDEX `fk_application_user_applicantId_idx` (`applicantId` ASC) VISIBLE,
    CONSTRAINT `fk_campaign_application_user_createdBy`
        FOREIGN KEY (`createdBy`)
            REFERENCES `teachsync`.`user` (`id`),
    CONSTRAINT `fk_campaign_application_user_updatedBy`
        FOREIGN KEY (`updatedBy`)
            REFERENCES `teachsync`.`user` (`id`),
    CONSTRAINT `fk_application_campaign`
        FOREIGN KEY (`campaignId`)
            REFERENCES `teachsync`.`recruitment_campaign` (`id`),
    CONSTRAINT `fk_application_user_applicantId`
        FOREIGN KEY (`applicantId`)
            REFERENCES `teachsync`.`user` (`id`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teachsync`.`application_detail`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teachsync`.`application_detail`;

CREATE TABLE IF NOT EXISTS `teachsync`.`application_detail`
(
    `id`            BIGINT      NOT NULL,
    `applicationId` BIGINT      NOT NULL,
    `detailType`    VARCHAR(45) NOT NULL COMMENT 'cv, id, degree, ...',
    `detailLink`    LONGTEXT    NOT NULL COMMENT 'url to file',
    `detailNote`    LONGTEXT    NULL DEFAULT NULL,
    `submitAt`      DATETIME    NULL DEFAULT NULL,
    `status`        VARCHAR(45) NOT NULL,
    `createdAt`     DATETIME    NULL DEFAULT NULL,
    `createdBy`     BIGINT      NULL DEFAULT NULL,
    `updatedAt`     DATETIME    NULL DEFAULT NULL,
    `updatedBy`     BIGINT      NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_application_detail_application_idx` (`applicationId` ASC) VISIBLE,
    INDEX `fk_application_detail_user_createdBy_idx` (`createdBy` ASC) VISIBLE,
    INDEX `fk_application_detail_user_updatedBy_idx` (`updatedBy` ASC) VISIBLE,
    CONSTRAINT `fk_application_detail_application`
        FOREIGN KEY (`applicationId`)
            REFERENCES `teachsync`.`campaign_application` (`id`),
    CONSTRAINT `fk_application_detail_user_createdBy`
        FOREIGN KEY (`createdBy`)
            REFERENCES `teachsync`.`user` (`id`),
    CONSTRAINT `fk_application_detail_user_updatedBy`
        FOREIGN KEY (`updatedBy`)
            REFERENCES `teachsync`.`user` (`id`)
)
    ENGINE = InnoDB;


CREATE TABLE `testrecord` (
                              `id` bigint NOT NULL AUTO_INCREMENT,
                              `testId` bigint NOT NULL,
                              `userId` bigint NOT NULL,
                              `username` varchar(45) DEFAULT NULL,
                              `class` varchar(45) DEFAULT NULL,
                              `questionId` bigint NOT NULL,
                              `questionType` varchar(45) DEFAULT NULL,
                              `essayAnswer` varchar(4000) DEFAULT NULL,
                              `answerMCId` bigint DEFAULT NULL,
                              `correct` bit(1) DEFAULT NULL,
                              PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `test_session` (
                                `id` bigint NOT NULL AUTO_INCREMENT,
                                `userid` bigint NOT NULL,
                                `username` varchar(100) NOT NULL,
                                `testid` bigint DEFAULT NULL,
                                `subject` varchar(200) DEFAULT NULL,
                                `class` varchar(45) DEFAULT NULL,
                                `start_date` datetime DEFAULT NULL,
                                `submit_date` datetime DEFAULT NULL,
                                `status` bigint NOT NULL,
                                `update_date` datetime DEFAULT NULL,
                                `user_update` varchar(200) DEFAULT NULL,
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



SET SQL_MODE = @OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS;
