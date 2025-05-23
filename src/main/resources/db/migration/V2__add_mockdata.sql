-- Insert mock data into bucketinfos
INSERT INTO bucketinfos (idExterne, url, tag1, tag2, tag3, date_create)
VALUES (UUID(), 'https://example.com/image1.jpg', 'nature', 'outdoor', 'summer', NOW()),
       (UUID(), 'https://example.com/image2.jpg', 'urban', 'architecture', 'night', NOW()),
       (UUID(), 'https://example.com/image3.jpg', 'portrait', 'studio', 'blackwhite', NOW());

-- Insert mock data into idea
INSERT INTO idea (title, description, tag1, tag2, image)
VALUES ('Eco Housing', 'Sustainable housing design for eco-friendly living.', 'sustainability', 'architecture',
        'eco1.jpg'),
       ('Smart Traffic System', 'AI-based traffic lights to optimize flow.', 'AI', 'transportation', 'traffic_ai.jpg'),
       ('Vertical Farming', 'Grow crops in high-rise buildings in cities.', 'agriculture', 'urban',
        'farm_vertical.jpg');

-- Insert mock data into request_input
INSERT INTO request_input (person, reference, type, idea_id)
VALUES ('Alice Smith', 'Movie1', 1, 1),
       ('Bob Johnson', 'Movie2', 2, 2),
       ('Charlie Lee', 'Serie1', 3, 3);
