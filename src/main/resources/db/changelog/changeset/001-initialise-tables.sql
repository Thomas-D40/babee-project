--liquibase formatted sql
--changeset thomas-darracq:001-initialise-schema

CREATE TABLE IF NOT EXISTS BABEE (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    prenom VARCHAR(255) NOT NULL,
    nom VARCHAR(255) NOT NULL,
    date_naissance DATE NOT NULL,
    photo BYTEA
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
    commentaire VARCHAR(255),
    FOREIGN KEY (babee_id) REFERENCES BABEE(id)
);

CREATE TABLE IF NOT EXISTS HEALTHACT (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    babee_id UUID NOT NULL,
    event_date DATE NOT NULL,
    health_act_type INTEGER NOT NULL,
    temperature INTEGER,
    nom_medicament VARCHAR(255),
    dosage VARCHAR(255),
    FOREIGN KEY (babee_id) REFERENCES BABEE(id)
);

CREATE TABLE IF NOT EXISTS SLEEPING (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    babee_id UUID NOT NULL,
    event_date DATE NOT NULL,
    debut TIME NOT NULL,
    fin TIME NOT NULL,
    FOREIGN KEY (babee_id) REFERENCES BABEE(id)
);

CREATE TABLE IF NOT EXISTS CAREACT (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    babee_id UUID NOT NULL,
    event_date DATE NOT NULL,
    care_act_type INTEGER NOT NULL,
    care_act_detail INTEGER,
    commentaire VARCHAR(255),
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
    commentaire VARCHAR(255) NOT NULL,
    FOREIGN KEY (babee_id) REFERENCES BABEE(id)
);
