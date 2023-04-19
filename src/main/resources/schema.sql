DROP TABLE IF EXISTS bookings, comments, items, users CASCADE ;

CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(512) NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (user_id),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);
CREATE TABLE IF NOT EXISTS items (
    item_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(200) NOT NULL,
    is_available BOOLEAN,
    owner_id BIGINT REFERENCES users(user_id) ON DELETE SET NULL,
    CONSTRAINT pk_items PRIMARY KEY (item_id)
);
CREATE TABLE IF NOT EXISTS comments (
    comment_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    text VARCHAR(250) NOT NULL,
    item_id BIGINT REFERENCES items(item_id) ON DELETE CASCADE,
    author_name VARCHAR(100) NOT NULL,
    created TIMESTAMP NOT NULL,
    CONSTRAINT pk_comments PRIMARY KEY (comment_id)
);
CREATE TABLE IF NOT EXISTS bookings (
    booking_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL,
    booker_id BIGINT REFERENCES users(user_id),
    item_id BIGINT REFERENCES items(item_id),
    CONSTRAINT pk_bookings PRIMARY KEY (booking_id)
);