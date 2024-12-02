MERGE INTO member (user_id, password, email, name, ROLE, created_at, updated_at)
    KEY (user_id)
    VALUES ('user1', '$2a$10$z1NLrjhkoPJYzHucx7XVDeENGlYgs1zPt/hQIgtAqentc.Qrkv7m6',
            'user1@naver.com',
            'user1', 'USER', now(), NULL);
MERGE INTO member (user_id, password, email, name, ROLE, created_at, updated_at)
    KEY (user_id)
    VALUES ('user2', '$2a$10$z1NLrjhkoPJYzHucx7XVDeENGlYgs1zPt/hQIgtAqentc.Qrkv7m6',
            'user2@google.com',
            'user2', 'USER', now(), NULL);