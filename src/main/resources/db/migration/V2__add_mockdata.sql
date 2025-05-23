-- Insert mock data into idea
INSERT INTO idea (title, description, idExterne, image, pdf, tag1, tag2, type, date_create)
VALUES
    ('Eco Housing', 'Sustainable housing design for eco-friendly living.', '123e4567-e89b-12d3-a456-426614174000', 'eco1.jpg', 'eco_housing.pdf', 'sustainability', 'architecture', 'concept', CURRENT_TIMESTAMP),
    ('Smart Traffic System', 'AI-based traffic lights to optimize flow.', '123e4567-e89b-12d3-a456-426614174001', 'traffic_ai.jpg', 'smart_traffic.pdf', 'AI', 'transportation', 'innovation', CURRENT_TIMESTAMP),
    ('Vertical Farming', 'Grow crops in high-rise buildings in cities.', '123e4567-e89b-12d3-a456-426614174002', 'farm_vertical.jpg', 'vertical_farming.pdf', 'agriculture', 'urban', 'solution', CURRENT_TIMESTAMP);

-- Insert mock data into request_input
INSERT INTO request_input (person, reference, type, idea_id)
VALUES
    ('Alice Smith', 'Movie1', 1, 1),
    ('Bob Johnson', 'Movie2', 2, 2),
    ('Charlie Lee', 'Serie1', 3, 3);
