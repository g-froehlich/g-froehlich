-- database PickleBallLeague
BEGIN TRANSACTION;

-- *************************************************************************************************
-- Drop all db objects in the proper order
-- *************************************************************************************************

DROP TABLE IF EXISTS player_match;
DROP TABLE IF EXISTS team_match;
DROP TABLE IF EXISTS league_team;
DROP TABLE IF EXISTS league_player;
DROP TABLE IF EXISTS team_player;
DROP TABLE IF EXISTS team;
DROP TABLE IF EXISTS player;
DROP TABLE IF EXISTS league;
DROP TABLE IF EXISTS court;
DROP TABLE IF EXISTS users;

-- *************************************************************************************************
-- Create the tables and constraints
-- *************************************************************************************************

--users (name is pluralized because 'user' is a SQL keyword)
CREATE TABLE users (
    user_id SERIAL,
    username varchar(50) NOT NULL UNIQUE,
    password_hash varchar(200) NOT NULL,
    role varchar(50) NOT NULL,
    name varchar(50) NOT NULL,
    address varchar(100) NULL,
    city varchar(50) NULL,
    state_code char(2) NULL,
    zip varchar(5) NULL,
    CONSTRAINT PK_user PRIMARY KEY (user_id)
);

--court
--(contains valid locations for leagues)
CREATE TABLE court (
    court_id SERIAL,
    court_address varchar(100) NOT NULL,
    court_name varchar(50) NOT NULL,
    CONSTRAINT PK_court PRIMARY KEY (court_id)
);

--league
--(contains location of league)
CREATE TABLE league (
    league_id SERIAL,
	manager_id int NOT NULL,
    court_id int NOT NULL,
    league_name varchar(100) NOT NULL,
    league_start_date date NOT NULL,
    league_end_date date NULL,
    CONSTRAINT PK_league PRIMARY KEY (league_id),
    CONSTRAINT FK_court FOREIGN KEY (court_id) REFERENCES court(court_id),
	CONSTRAINT FK_league_manager FOREIGN KEY (manager_id) REFERENCES users(user_id)
);

--player
--(contains cumulative score, name, W/L ratio)
CREATE TABLE player (
    player_id SERIAL,
	user_id int NOT NULL,
    player_first_name varchar(50) NOT NULL,
    player_last_name varchar(50) NOT NULL,
    total_score int DEFAULT 0,
    win_loss_ratio DECIMAL(5,2) DEFAULT 0.00,
    CONSTRAINT PK_player PRIMARY KEY (player_id),
	CONSTRAINT FK_user FOREIGN KEY (user_id) REFERENCES users(user_id)
);

--team
--(contains cumulative score, name, W/L ratio)
CREATE TABLE team (
    team_id SERIAL,
	manager_id int NOT NULL,
    team_name varchar(50) NOT NULL,
    total_score int DEFAULT 0,
    win_loss_ratio DECIMAL(5,2) DEFAULT 0.00,
    CONSTRAINT PK_team PRIMARY KEY (team_id),
	CONSTRAINT FK_team_manager FOREIGN KEY (manager_id) REFERENCES users(user_id)
);

--team_player 
--(link table associating players to a team)
CREATE TABLE team_player (
    team_player_id SERIAL,
    player_id int NOT NULL,
    team_id int NOT NULL,
    CONSTRAINT PK_team_player PRIMARY KEY (team_player_id),
    CONSTRAINT FK_team_player_player FOREIGN KEY (player_id) REFERENCES player(player_id),
    CONSTRAINT FK_team_player_team FOREIGN KEY (team_id) REFERENCES team(team_id)
);
CREATE UNIQUE INDEX IX_unique_team_player ON team_player(player_id, team_id);

--league_player
--(relates a player to a league)
CREATE TABLE league_player (
    league_player_id SERIAL,
    league_id int NOT NULL,
    player_id int NOT NULL,
    CONSTRAINT PK_league_player PRIMARY KEY (league_player_id),
    CONSTRAINT FK_league_player_player FOREIGN KEY (player_id) REFERENCES player(player_id),
    CONSTRAINT FK_league_player_league FOREIGN KEY (league_id) REFERENCES league(league_id)
);
CREATE UNIQUE INDEX IX_unique_league_player ON league_player(league_id, player_id);

--league_team
--(relates a team to a league)
CREATE TABLE league_team (
    league_team_id SERIAL,
    league_id int NOT NULL,
    team_id int NOT NULL,
    CONSTRAINT PK_league_team PRIMARY KEY (league_team_id),
    CONSTRAINT FK_league_team_team FOREIGN KEY (team_id) REFERENCES team(team_id),
    CONSTRAINT FK_league_team_league FOREIGN KEY (league_id) REFERENCES league(league_id)
);
CREATE UNIQUE INDEX IX_unique_league_team ON league_team(league_id, team_id);

--team_match
--(contains match data: teams, scores, league, match_date)
CREATE TABLE team_match (
    match_id SERIAL,
    league_id INT NOT NULL,
    match_date DATE NOT NULL,
    team1_id INT NOT NULL,
    team2_id INT NOT NULL,
    team1_score INT DEFAULT 0,
    team2_score INT DEFAULT 0,
    CONSTRAINT PK_team_match PRIMARY KEY (match_id),
    CONSTRAINT FK_team_match_league FOREIGN KEY (league_id) REFERENCES league(league_id),
    CONSTRAINT FK_team_match_team1 FOREIGN KEY (team1_id) REFERENCES team(team_id),
    CONSTRAINT FK_team_match_team2 FOREIGN KEY (team2_id) REFERENCES team(team_id),
    CONSTRAINT UQ_team_match UNIQUE (league_id, match_date, team1_id, team2_id)
);

--player_match
--(contains match data: players, scores, league, match_date)
CREATE TABLE player_match (
    match_id SERIAL,
    league_id INT NOT NULL,
    match_date DATE NOT NULL,
    player1_id INT NOT NULL,
    player2_id INT NOT NULL,
    player1_score INT DEFAULT 0,
    player2_score INT DEFAULT 0,
    CONSTRAINT PK_player_match PRIMARY KEY (match_id),
    CONSTRAINT FK_player_match_league FOREIGN KEY (league_id) REFERENCES league(league_id),
    CONSTRAINT FK_player_match_player1 FOREIGN KEY (player1_id) REFERENCES player(player_id),
    CONSTRAINT FK_player_match_player2 FOREIGN KEY (player2_id) REFERENCES player(player_id),
    CONSTRAINT UQ_player_match UNIQUE (league_id, match_date, player1_id, player2_id)
);

-- *************************************************************************************************
-- Insert some sample starting data
-- *************************************************************************************************

-- Users
-- Password for all users is password
INSERT INTO users (username, password_hash, role, name, address, city, state_code, zip) VALUES 
    ('player', '$2a$10$tmxuYYg1f5T0eXsTPlq/V.DJUKmRHyFbJ.o.liI1T35TFbjs2xiem','ROLE_PLAYER',  'Example Player', null, 'Cincinnati', 'OH', '45202'),
    ('league_manager','$2a$10$tmxuYYg1f5T0eXsTPlq/V.DJUKmRHyFbJ.o.liI1T35TFbjs2xiem', 'ROLE_LEAGUE_MANAGER', 'Example League Manager', null, 'Cincinnati', 'OH', '45202'),
    ('admin','$2a$10$tmxuYYg1f5T0eXsTPlq/V.DJUKmRHyFbJ.o.liI1T35TFbjs2xiem','ROLE_ADMIN', 'Example Admin', null, 'Cincinnati', 'OH', '45202');

-- Insert data into court table
INSERT INTO court (court_address, court_name) VALUES 
    ('123 Main St, Cincinnati, OH', 'Central Court'),
    ('456 Elm St, Cincinnati, OH', 'Westside Court'),
    ('789 Maple Ave, Cincinnati, OH', 'Eastside Court');

-- Insert data into league table
INSERT INTO league (manager_id, court_id, league_name, league_start_date, league_end_date) VALUES 
    (2, 1, 'Summer Pickleball League', '2024-06-01', '2024-08-31'),
    (2, 2, 'Fall Pickleball League', '2024-09-01', '2024-11-30'),
    (2, 3, 'Winter Pickleball League', '2024-12-01', NULL);

-- Insert data into player table
INSERT INTO player (user_id, player_first_name, player_last_name) VALUES 
    (1, 'John', 'Doe'),
    (1, 'Jane', 'Smith'),
    (1, 'Emily', 'Jones'),
    (1, 'Michael', 'Brown');

-- Insert data into team table
INSERT INTO team (manager_id, team_name) VALUES 
    (1, 'The Smashers'),
    (1, 'Pickle Pals'),
    (1, 'Net Ninjas');

-- Insert data into team_player table
INSERT INTO team_player (player_id, team_id) VALUES 
    (1, 1),  -- John Doe -> The Smashers
    (2, 1),  -- Jane Smith -> The Smashers
    (3, 2),  -- Emily Jones -> Pickle Pals
    (4, 3);  -- Michael Brown -> Net Ninjas

-- Insert data into league_player table
INSERT INTO league_player (league_id, player_id) VALUES 
    (1, 1),  -- John Doe in Summer League
    (1, 2),  -- Jane Smith in Summer League
    (2, 3),  -- Emily Jones in Fall League
    (2, 4);  -- Michael Brown in Fall League

-- Insert data into league_team table
INSERT INTO league_team (league_id, team_id) VALUES 
    (1, 1),  -- The Smashers in Summer League
    (2, 2),  -- Pickle Pals in Fall League
    (2, 3);  -- Net Ninjas in Fall League

-- Insert data into team_match table
INSERT INTO team_match (league_id, match_date, team1_id, team2_id, team1_score, team2_score) VALUES 
    (1, '2024-06-15', 1, 2, 21, 15),  -- The Smashers vs. Pickle Pals
    (2, '2024-09-10', 2, 3, 18, 21);  -- Pickle Pals vs. Net Ninjas

-- Insert data into player_match table
INSERT INTO player_match (league_id, match_date, player1_id, player2_id, player1_score, player2_score) VALUES 
    (1, '2024-06-16', 1, 2, 21, 19),  -- John Doe vs. Jane Smith
    (2, '2024-09-12', 3, 4, 15, 21);  -- Emily Jones vs. Michael Brown

COMMIT TRANSACTION;
