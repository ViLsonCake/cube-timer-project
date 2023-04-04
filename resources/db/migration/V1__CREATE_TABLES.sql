create table hibernate_sequence (
    next_val bigint
) engine=MyISAM;

insert into hibernate_sequence values ( 1 );
insert into hibernate_sequence values ( 1 );

CREATE TABLE user (
    user_id BIGINT NOT NULL AUTO_INCREMENT,
    username varchar(64) NOT NULL UNIQUE,
    password varchar(64) NOT NULL,
    email varchar(64) NOT NULL UNIQUE,

    PRIMARY KEY (user_id)
) engine=MyISAM;

CREATE TABLE solve (
    solve_id BIGINT NOT NULL AUTO_INCREMENT,
    scramble varchar(64) NOT NULL,
    cube_variable varchar(64) NOT NULL,
    time varchar(64) NOT NULL,

    PRIMARY KEY (solve_id)
) engine=MyISAM;