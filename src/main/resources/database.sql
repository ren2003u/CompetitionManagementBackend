-- Create database
DROP DATABASE IF EXISTS competition_management_change;
CREATE DATABASE competition_management_change;

-- Use database
USE competition_management_change;

-- Create table event_information
CREATE TABLE event_information (
  event_number INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  event_name VARCHAR(255) NOT NULL UNIQUE,
  event_time VARCHAR(255) NOT NULL,
  event_location VARCHAR(255)
);


-- Create table team_information
CREATE TABLE team_information (
  team_number INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  team_name VARCHAR(255) NOT NULL UNIQUE,
  team_affiliation_college VARCHAR(255),
  total_score INT(11)
);

-- Create table user
CREATE TABLE user (
  id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
team_name VARCHAR(255) NOT NULL default '' ,
  username VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  status VARCHAR(255) NOT NULL,
score INT(11) default 0
);


-- Create table event_team
CREATE TABLE event_team (
  id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  event_number INT(11),
  team_number INT(11),
  FOREIGN KEY (event_number) REFERENCES event_information(event_number),
  FOREIGN KEY (team_number) REFERENCES team_information(team_number)
);


-- Insert two users
INSERT INTO user (username, password, status) VALUES ('admin', '111', 'admin');
INSERT INTO user (username, password, status) VALUES ('user', '111', 'player');