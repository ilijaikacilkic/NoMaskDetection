insert into users (username, password, name, last_name) values ("admin", "$2a$12$agtfUW0Si5DU3W6emMqBXumr/aVFAx9RUJS/HIycn8sdVndsjlalC", "John", "Doe");
insert into authorities (id, name) values (1, "UPLOAD_IMAGE");
insert into user_authorities (user_username, authority_id) values ("admin", 1);

insert into users (username, password, name, last_name) values ("admin2", "$2a$12$27V8p87aT2FSY9v9WBjH2OL3Fa02duMVYH7mK9u9scitxoROLhJbW", "Jane", "Doe");
insert into authorities (id, name) values (2, "READ_IMAGE");
insert into authorities (id, name) values (3, "DELETE_IMAGE")
insert into user_authorities (user_username, authority_id) values ("admin2", 2);
insert into user_authorities (user_username, authority_id) values ("admin2", 3);