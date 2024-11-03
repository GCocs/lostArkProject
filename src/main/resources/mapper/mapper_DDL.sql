-- 9.24

CREATE DATABASE LOSTARKPROJECT;
USE LOSTARKPROJECT;


CREATE TABLE USER_CHARACTER(
    CHARACTER_NAME VARCHAR(50) PRIMARY KEY NOT NULL,
    SERVER_NAME VARCHAR(30) NOT NULL,
    CHARACTER_CLASS_NAME VARCHAR(30) NOT NULL,
    ITEMAVGLEVEL BIGINT NOT NULL,
    ITEMMAXLEVEL BIGINT NOT NULL,
    EXPEDITIONLEVEL BIGINT NOT NULL
);

CREATE TABLE CALENDAR(
    CALENDAR_ID BIGINT PRIMARY KEY NOT NULL,
    CATEGORY_NAME VARCHAR(100) NOT NULL,
    CONTENTS_NAME VARCHAR(100) NOT NULL,
    CONTENTS_ICON VARCHAR(255) NOT NULL,
    MIN_ITEM_LEVEL INT NOT NULL,
    LOCATION VARCHAR(100) NOT NULL
);

CREATE TABLE STARTTIME(
    START_TIME_ID BIGINT PRIMARY KEY NOT NULL,
    CALENDAR_ID BIGINT NOT NULL,
    START_TIME DATETIME NOT NULL,
    FOREIGN KEY (CALENDAR_ID) REFERENCES CALENDAR(CALENDAR_ID)
);

CREATE TABLE REWARDITEM(
    REWARD_ITEM_ID BIGINT PRIMARY KEY NOT NULL,
    CALENDAR_ID BIGINT NOT NULL,
    ITEM_LEVEL INT NOT NULL,
    FOREIGN KEY (CALENDAR_ID) REFERENCES CALENDAR(CALENDAR_ID)
);

CREATE TABLE ITEM(
    ITEM_ID BIGINT PRIMARY KEY NOT NULL,
    REWARD_ITEM_ID BIGINT NOT NULL,
    NAME VARCHAR(100) NOT NULL,
    ICON VARCHAR(255) NOT NULL,
    GRADE VARCHAR(30) NOT NULL,
    FOREIGN KEY (REWARD_ITEM_ID) REFERENCES REWARDITEM(REWARD_ITEM_ID)
);

CREATE TABLE COLLECTABLE_DETAIL_POINT(
    COLLECTABLE_DETAIL_NAME VARCHAR(100) NOT NULL PRIMARY KEY,
    COLLECTABLE_DETAIL_COLLECTION_STATUS BOOLEAN NOT NULL
);

CREATE TABLE COLLECTABLE(
    COLLECTABLE_ID INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    COLLECTABLE_NAME VARCHAR(50) NOT NULL,
    COLLECTABLE_ICON VARCHAR(255) NOT NULL,
    COLLECTABLE_MAXPOINT INT NOT NULL,
    COLLECTABLE_POINT INT NOT NULL,
    COLLECTABLE_DETAIL_NAME VARCHAR(100) NOT NULL,
    FOREIGN KEY (COLLECTABLE_DETAIL_NAME) REFERENCES COLLECTABLE_DETAIL_POINT(COLLECTABLE_DETAIL_NAME)
);

CREATE TABLE USER (
    USER_ID VARCHAR(50) PRIMARY KEY NOT NULL,
    USER_PW VARCHAR(50) NOT NULL,
    JOIN_AT DATE NOT NULL,
    AUTHORIZATION BOOLEAN NOT NULL,
    REPRESENTATIVE_CHARACTER_NAME VARCHAR(50) NOT NULL,
    CHARACTER_NAME VARCHAR(50) NOT NULL,
    CONTENT_NAME VARCHAR(255)  NOT NULL,
    COLLECTABLE_ID INT NOT NULL,
    FOREIGN KEY(CHARACTER_NAME) REFERENCES USER_CHARACTER(CHARACTER_NAME),
    FOREIGN KEY(CONTENT_NAME) REFERENCES CONTENT(CONTENT_NAME),
    FOREIGN KEY(COLLECTABLE_ID) REFERENCES COLLECTABLE(COLLECTABLE_ID)
);

CREATE TABLE NOTICE(
    NOTICE_ID BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    NOTICE_TITLE VARCHAR(255) NOT NULL,
    NOTICE_CONTENT VARCHAR(255) NOT NULL,
    POSTED_AT DATE NOT NULL
);

CREATE TABLE ADMIN(
    ADMIN_ID VARCHAR(255) NOT NULL,
    ADMIN_PW VARCHAR(255) NOT NULL
);



-- 언제임

CREATE DATABASE LOSTARKPROJECT;
USE LOSTARKPROJECT;

CREATE TABLE CONTENT (
    CONTENT_NAME VARCHAR(255) PRIMARY KEY NOT NULL,
    CONTENT_ALARMSETTINGSTATUS CHAR(1) UNIQUE NOT NULL,
    CONTENT_CATEGORY VARCHAR(30) NOT NULL,
    CONTENT_ICON VARCHAR(255) NOT NULL,
    CONTENT_MINITEMLEVEL INTEGER NOT NULL,
    CONTENT_STARTTIME DATE DEFAULT (CURRENT_DATE) NOT NULL
);

CREATE TABLE USER (
    CHARACTERNAME VARCHAR(200) PRIMARY KEY NOT NULL,
    USER_ID VARCHAR(100) NOT NULL,
    USER_PW VARCHAR(255) NOT NULL,
    JOIN_AT DATE NOT NULL,
    CONTENT_ALARMSETTINGSTATUS CHAR(1) NOT NULL,
    FOREIGN KEY (CONTENT_ALARMSETTINGSTATUS) REFERENCES CONTENT(CONTENT_ALARMSETTINGSTATUS)
);

CREATE TABLE USER_CHARACTER (
    CHARACTERNAME VARCHAR(200) NOT NULL,
    SERVERNAME VARCHAR(30) NOT NULL,
    CHARACTERCLASSNAME VARCHAR(30) NOT NULL,
    ITEMAVGLEVEL INTEGER NOT NULL,
    SIBLINGS VARCHAR(200) NOT NULL,
    ITEMMAXLEVEL INTEGER NOT NULL,
    EXPEDITIONLEVEL INTEGER NOT NULL,
    FOREIGN KEY (CHARACTERNAME) REFERENCES USER(CHARACTERNAME)
);

CREATE TABLE COLLECTIBLE (


);


