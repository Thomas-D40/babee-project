--liquibase formatted sql
--changeset thomas-darracq:001-initialise-schema

CREATE TABLE IF NOT EXISTS BABEE (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    birth_date DATE NOT NULL,
    photo_url VARCHAR(255)
);


CREATE TABLE IF NOT EXISTS PARENT (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    babee_id UUID NOT NULL,
    FOREIGN KEY (babee_id) REFERENCES BABEE(id)
);

CREATE TABLE IF NOT EXISTS ACTIVITY (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    babee_id UUID NOT NULL,
    event_date DATE NOT NULL,
    name VARCHAR(255) NOT NULL,
    comment VARCHAR(255),
    FOREIGN KEY (babee_id) REFERENCES BABEE(id)
);

CREATE TABLE IF NOT EXISTS HEALTHACT (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    babee_id UUID NOT NULL,
    event_date DATE NOT NULL,
    health_act_type INTEGER NOT NULL,
    temperature INTEGER,
    medecine VARCHAR(255),
    dosage VARCHAR(255),
    act_hour TIME NOT NULL,
    FOREIGN KEY (babee_id) REFERENCES BABEE(id)
);

CREATE TABLE IF NOT EXISTS SLEEPING (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    babee_id UUID NOT NULL,
    event_date DATE NOT NULL,
    begin_hour TIME NOT NULL,
    end_hour TIME NOT NULL,
    FOREIGN KEY (babee_id) REFERENCES BABEE(id)
);

CREATE TABLE IF NOT EXISTS CAREACT (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    babee_id UUID NOT NULL,
    event_date DATE NOT NULL,
    care_act_type INTEGER NOT NULL,
    care_act_detail INTEGER,
    comment VARCHAR(255),
    FOREIGN KEY (babee_id) REFERENCES BABEE(id)
);

CREATE TABLE IF NOT EXISTS FEEDING (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    babee_id UUID NOT NULL,
    event_date DATE NOT NULL,
    feeding_informations JSONB NOT NULL,
    FOREIGN KEY (babee_id) REFERENCES BABEE(id)
);

CREATE TABLE IF NOT EXISTS INFORMATION (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    babee_id UUID NOT NULL,
    event_date DATE NOT NULL,
    comment VARCHAR(255) NOT NULL,
    FOREIGN KEY (babee_id) REFERENCES BABEE(id)
);
