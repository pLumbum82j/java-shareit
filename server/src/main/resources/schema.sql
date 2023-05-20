DROP TABLE IF EXISTS bookings, comments, items, requests, users CASCADE ;

CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(512) NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (user_id),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS requests (
    request_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    description VARCHAR(512) NOT NULL,
    requestor_id BIGINT NOT NULL,
    created TIMESTAMP NOT NULL,
    CONSTRAINT pk_request PRIMARY KEY (request_id),
    FOREIGN KEY (requestor_id) REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS items (
    item_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(200) NOT NULL,
    is_available BOOLEAN,
    owner_id BIGINT REFERENCES users(user_id) ON DELETE CASCADE,
    request_id BIGINT REFERENCES requests(request_id),
    CONSTRAINT pk_item PRIMARY KEY (item_id),
    FOREIGN KEY (request_id) REFERENCES requests (request_id)
);

CREATE TABLE IF NOT EXISTS comments (
    comment_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    text VARCHAR(250) NOT NULL,
    item_id BIGINT REFERENCES items(item_id) ON DELETE CASCADE,
    author_id BIGINT REFERENCES users(user_id) ON DELETE CASCADE,
    created TIMESTAMP NOT NULL,
    CONSTRAINT pk_comment PRIMARY KEY (comment_id)
);

CREATE TABLE IF NOT EXISTS bookings (
    booking_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    item_id BIGINT REFERENCES items(item_id),
    booker_id BIGINT REFERENCES users(user_id),
    status VARCHAR(20) NOT NULL,
    CONSTRAINT pk_booking PRIMARY KEY (booking_id)
);