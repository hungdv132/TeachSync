use teachsync;

insert into location_unit(parentId, level, unitName, unitAlias, unitType, status)
values (null, 0, 'Vietnam', 'VN', 'COUNTRY', 'CREATED');
insert into location_unit(parentId, level, unitName, unitAlias, unitType, status)
values (1, 1, 'Hanoi', 'TP.HN', 'MUNICIPALITY', 'CREATED'),
       (1, 1, 'Ho Chi Minh', 'TP.HCM', 'MUNICIPALITY', 'CREATED');
insert into location_unit(parentId, level, unitName, unitAlias, unitType, status)
values (2, 2, 'Ba Dinh', 'Q.Ba Dinh', 'DISTRICT', 'CREATED'),
       (2, 2, 'Cau Giay', 'Q.Cau Giay', 'DISTRICT', 'CREATED'),

       (3, 2, '1', 'Q.1', 'DISTRICT', 'CREATED'),
       (3, 2, '3', 'Q.3', 'DISTRICT', 'CREATED');
insert into location_unit(parentId, level, unitName, unitAlias, unitType, status)
values (4, 3, 'Doi Can', 'P.Doi Can', 'WARD', 'CREATED'),
       (4, 3, 'Ngoc Ha', 'P.Ngoc Ha', 'WARD', 'CREATED'),

       (5, 3, 'Dich Vong', 'P.Dich Vong', 'WARD', 'CREATED'),
       (5, 3, 'Mai Dich', 'P.Mai Dich', 'WARD', 'CREATED'),

       (6, 3, 'Ben Thanh', 'P.Ben Thanh', 'WARD', 'CREATED'),
       (6, 3, 'Tan Dinh', 'P.Tan Dinh', 'WARD', 'CREATED'),

       (7, 3, '1', 'P.1', 'WARD', 'CREATED'),
       (7, 3, '2', 'P.2', 'WARD', 'CREATED');

insert into address(addressNo, street, unitId, addressString, status)
values ('190ABC', 'Doi Can', 8, '190ABC Doi Can, P.Doi Can, Q.Ba Dinh, TP.HN, VN', 'CREATED'),
       ('4DEF', 'Ngoc Ha', 9, '4DEF Ngoc Ha, P.Ngoc Ha, Q.Ba Dinh, TP.HN, HN, VN', 'CREATED'),

       ('337GHI', 'Cau Giay', 8, '337GHI Cau Giay, P.Dich Vong, Q.Cau Giay, TP.HN, VN', 'CREATED'),
       ('31JKL', 'Ho Tung Mau', 8, '31JKL Ho Tung Mau, P.Mai Dich, Q.Cau Giay, TP.HN, VN', 'CREATED');

insert into role(roleName, roleDesc, status)
values ('Student', 'Student', 'CREATED'),
       ('Parent', 'Parent', 'CREATED'),
       ('Teacher', 'Teacher', 'CREATED'),
       ('Admin', 'Admin', 'CREATED'),
       ('Staff', 'Staff', 'CREATED');

insert into user(username, password, roleId, userAvatar, fullName, email,
                 phone, addressId, resetPasswordToken, parentId, status)
values ('student', '$2a$10$2muXk6hqYaoTLnXWCszDaeDE71FGa2hJkh3QPaGKJLRJ77bm6GFJW', 1, null, 'Test Student',
        'test.student@gmail.com', null, null, null, null, 'CREATED'),
       ('parent', '$2a$10$2muXk6hqYaoTLnXWCszDaeDE71FGa2hJkh3QPaGKJLRJ77bm6GFJW', 2, null, 'Test Parent',
        'test.parent@gmail.com', null, null, null, null, 'CREATED'),
       ('teacher', '$2a$10$2muXk6hqYaoTLnXWCszDaeDE71FGa2hJkh3QPaGKJLRJ77bm6GFJW', 3, null, 'Test Teacher',
        'test.teacher@gmail.com', null, null, null, null, 'CREATED'),
       ('admin', '$2a$10$2muXk6hqYaoTLnXWCszDaeDE71FGa2hJkh3QPaGKJLRJ77bm6GFJW', 4, null, 'Test Admin',
        'test.admin@gmail.com', null, null, null, null, 'CREATED'),
       ('staff', '$2a$10$2muXk6hqYaoTLnXWCszDaeDE71FGa2hJkh3QPaGKJLRJ77bm6GFJW', 5, null, 'Test Staff',
        'test.staff@gmail.com', null, null, null, null, 'CREATED'),
       ('teacher2', '$2a$10$2muXk6hqYaoTLnXWCszDaeDE71FGa2hJkh3QPaGKJLRJ77bm6GFJW', 3, null, 'Test Teacher2',
        'test.teacher2@gmail.com', null, null, null, null, 'CREATED');
--     Password = 123456789

insert into center(addressId, centerName, centerType, centerDesc, centerSize, status)
values (1, 'TeachSync Doi Can', 'ENGLISH', null, 20, 'CREATED'),
       (4, 'TeachSync Mai Dich', 'ENGLISH', null, 25, 'CREATED');

insert into room(centerId, roomType, roomDesc, roomName, roomSize, status)
values (1, 'CLASSROOM', null, '101', 30, 'CREATED'),
       (1, 'CLASSROOM', null, '102', 20, 'CREATED'),
       (1, 'CLASSROOM', null, '103', 20, 'CREATED'),
       (1, 'CLASSROOM', null, '104', 40, 'CREATED'),
       (1, 'CLASSROOM', null, '105', 30, 'CREATED'),

       (2, 'CLASSROOM', null, '101', 25, 'CREATED'),
       (2, 'CLASSROOM', null, '102', 25, 'CREATED'),
       (2, 'CLASSROOM', null, '103', 25, 'CREATED'),
       (2, 'CLASSROOM', null, '104', 25, 'CREATED'),
       (2, 'CLASSROOM', null, '105', 20, 'CREATED');

insert into staff(centerId, userId, staffType, status)
values (1, 3, 'TEACHER', 'CREATED'),
       (2, 6, 'TEACHER', 'CREATED');

insert into course(courseName, courseAlias, courseImg, courseDesc, numSession, minScore, minAttendant, status)
values ('ielts 101', 'ILT101',
        'https://th.bing.com/th/id/OIP.R7Wj-CVruj2Gcx-MmaxmZAHaKe?pid=ImgDet&rs=1',
        null, 14, 5.0, 80, 'CREATED'),
       ('English Grade 1', 'ENG001',
        'https://th.bing.com/th/id/OIP.isjamm3juJANzM_sHKCx0wHaKe?pid=ImgDet&rs=1',
        null, 12, 4.5, 75, 'CREATED'),
       ('English Grade 2', 'ENG002',
        'https://www.geo.tv/assets/uploads/updates/2021-05-03/348462_790220_updates.jpg',
        null, 12, 5.0, 75, 'CREATED'),
       ('English Grade 3', 'ENG003',
        'https://th.bing.com/th/id/OIP.H1Q3_d4okF70VIVYp-YA5AAAAA?pid=ImgDet&w=360&h=470&rs=1',
        null, 12, 5.0, 75, 'CREATED');

insert into price_log(courseId, price, isPromotion, promotionAmount, promotionType, promotionDesc, validFrom, validTo, status)
values (1, 200000, true, 15, 'PERCENT', 'Grand opening super deal, now cheaper ', '2023-06-10 00:00:01', null,'CREATED'),
       (2, 100000, true, 20000, 'AMOUNT', 'To help with the first step, now reduce', '2023-06-10 00:00:01', null,'CREATED'),
       (3, 125000, false, null, null, null, '2023-06-10 00:00:01', null, 'CREATED'),
       (4, 150000, false, null, null, null, '2023-06-10 00:00:01', null, 'CREATED');

insert into certificate(courseId, certificateName, certificateDesc, status)
values (1, 'Ielts 101', 'You have complete the Ielts 101 course', 'CREATED'),
       (2, 'Eng 1', 'You have complete the Eng 1 course', 'CREATED'),
       (3, 'Eng 2', 'You have complete the Eng 2 course', 'CREATED'),
       (4, 'Eng 3', 'You have complete the Eng 3 course', 'CREATED');

insert into semester(semesterName, semesterAlias, semesterDesc, semesterType, startDate, endDate, status)
values ('Spring 23','SP23','Spring 23', 'SEASON', '2023-01-05', '2023-03-05', 'CREATED'),
       ('Summer 23','SU23','Summer 23', 'SEASON', '2023-03-10', '2023-06-10', 'CREATED'),
       ('Fall 23', 'FA23', 'Fall 23', 'SEASON', '2023-06-15', '2023-09-15', 'CREATED'),
       ('Winter 23', 'WI23', 'Winter 23', 'SEASON', '2023-09-15', '2023-12-15', 'CREATED');

insert into course_semester(courseId, semesterId, centerId, status)
values
    (1, 1, 1, 'CREATED'),
    (1, 2, 1, 'CREATED'),
    (1, 3, 1, 'CREATED'),
    (1, 4, 1, 'CREATED'),
    (2, 1, 1, 'CREATED'),
    (2, 2, 1, 'CREATED'),
    (2, 3, 1, 'CREATED'),
    (2, 4, 1, 'CREATED'),
    (3, 1, 1, 'CREATED'),
    (3, 2, 1, 'CREATED'),
    (3, 3, 1, 'CREATED'),
    (3, 4, 1, 'CREATED'),
    (4, 1, 1, 'CREATED'),
    (4, 2, 1, 'CREATED'),
    (4, 3, 1, 'CREATED'),

    (1, 1, 2, 'CREATED'),
    (1, 2, 2, 'CREATED'),
    (1, 3, 2, 'CREATED'),
    (1, 4, 2, 'CREATED'),
    (2, 1, 2, 'CREATED'),
    (2, 2, 2, 'CREATED'),
    (2, 3, 2, 'CREATED'),
    (2, 4, 2, 'CREATED'),
    (3, 1, 2, 'CREATED'),
    (3, 2, 2, 'CREATED'),
    (3, 3, 2, 'CREATED'),
    (3, 4, 2, 'CREATED'),
    (4, 1, 2, 'CREATED'),
    (4, 2, 2, 'CREATED'),
    (4, 3, 2, 'CREATED');

insert into clazz(courseSemesterId, staffId, clazzName, clazzDesc, clazzSize, status)
values (1, 1, 'Class 1 Ielts 101', null, 15, 'CREATED'),
       (2, 1, 'Class 2 Ielts 101', null, 15, 'CREATED'),

       (5, 1, 'Class 1 Eng gr 1', null, 30, 'CREATED'),
       (6, 1, 'Class 2 Eng gr 1', null, 30, 'CREATED'),

       (9, 2, 'Class 1 Eng gr 2', null, 30, 'CREATED'),
       (10, 2, 'Class 2 Eng gr 2', null, 30, 'CREATED'),

       (13, 2, 'Class 1 Eng gr 3', null, 25, 'CREATED'),
       (14, 2, 'Class 2 Eng gr 3', null, 25, 'CREATED');

insert into clazz_schedule(clazzId, roomId, scheduleType, startDate, endDate, slot, sessionStart, sessionEnd, status)
values (1, 1, 'MON_WED_FRI', '2023-06-10', '2023-08-10', 1, '07:00:00', '08:30:00', 'CREATED'),
       (2, 3, 'TUE_THU_SAT', '2023-06-10', '2023-08-10', 3, '10:30:00', '12:00:00', 'CREATED');

insert into session(roomId, scheduleId, staffId, slot, sessionStart, sessionEnd, status)
values (1, 1, 1, 1, '2023-06-12 07:00:00', '2023-06-12 08:30:00', 'CREATED'),
       (1, 1, 1, 1, '2023-06-14 07:00:00', '2023-06-14 08:30:00', 'CREATED'),
       (1, 1, 1, 1, '2023-06-16 07:00:00', '2023-06-16 08:30:00', 'CREATED'),
       (1, 1, 1, 1, '2023-06-19 07:00:00', '2023-06-19 08:30:00', 'CREATED'),
       (1, 1, 1, 1, '2023-06-21 07:00:00', '2023-06-21 08:30:00', 'CREATED'),
       (1, 1, 1, 1, '2023-06-23 07:00:00', '2023-06-23 08:30:00', 'CREATED'),
       (1, 1, 1, 1, '2023-06-26 07:00:00', '2023-06-26 08:30:00', 'CREATED'),
       (1, 1, 1, 1, '2023-06-28 07:00:00', '2023-06-28 08:30:00', 'CREATED'),
       (1, 1, 1, 1, '2023-06-30 07:00:00', '2023-06-30 08:30:00', 'CREATED'),
       (1, 1, 1, 1, '2023-07-03 07:00:00', '2023-07-03 08:30:00', 'CREATED'),
       (1, 1, 1, 1, '2023-07-05 07:00:00', '2023-07-05 08:30:00', 'CREATED'),
       (1, 1, 1, 1, '2023-07-07 07:00:00', '2023-07-07 08:30:00', 'CREATED');

insert into material(materialName, materialImg, materialLink, materialContent, materialType, isFree, status)
values ('Report1 template', '',
        'https://docs.google.com/document/d/1sfaOcv4sjc06r8QTElLPZroI6qcTINiS/edit?usp=drive_link&ouid=104632562605721525408&rtpof=true&sd=true',
        null, 'WORD', true, 'CREATED'),
       ('Report2 template', '',
        'https://docs.google.com/document/d/1E2Y6yXyRXhPe2td95RDszVZ8xY7WKdIg/edit?usp=drive_link&ouid=104632562605721525408&rtpof=true&sd=true',
        null, 'WORD', true, 'CREATED'),
       ('Report3 template', '',
        'https://docs.google.com/spreadsheets/d/17m8myHDMoVnW8hF4TVp6q-VVZWq1SO1J/edit?usp=drive_link&ouid=104632562605721525408&rtpof=true&sd=true',
        null, 'EXCEL', false, 'CREATED'),
       ('Report4 template', '',
        'https://docs.google.com/document/d/1bW8_eQIYvOIz3chOa20IqxjwrwVAnmJ1/edit?usp=drive_link&ouid=104632562605721525408&rtpof=true&sd=true',
        null, 'WORD', false, 'CREATED');

insert into course_material(courseId, materialId, status)
values (1, 1, 'CREATED'),
       (1, 2, 'CREATED'),
       (1, 3, 'CREATED'),
       (1, 4, 'CREATED'),

       (2, 1, 'CREATED'),
       (2, 2, 'CREATED'),
       (2, 4, 'CREATED'),

       (3, 3, 'CREATED'),
       (3, 4, 'CREATED');