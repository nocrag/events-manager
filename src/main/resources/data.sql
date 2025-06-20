USE springresto;

-- Layouts
INSERT INTO layout (id, created_date, description, is_archived, name, updated_date)
VALUES 
(1, NOW(), 'Main Hall Layout', 0, 'Main Hall', NOW()),
(2, NOW(), 'Outdoor Seating Layout', 0, 'Patio', NOW()),
(3, NOW(), 'Exclusive VIP layout with private booths', 0, 'VIP Lounge', NOW()),
(4, NOW(), 'Rooftop seating with skyline view', 0, 'Rooftop', NOW());

-- Menus
INSERT INTO menu (id, created_date, description, name)
VALUES 
(1, NOW(), 'Spring Specials - Light and Fresh', 'Spring Specials'),
(2, NOW(), 'Summer Favorites - Grilled and Chilled', 'Summer Favorites'),
(3, NOW(), 'Kids Menu - For our little guests', 'For Kids');


-- Dining Tables
INSERT INTO dining_table (id, number_of_seats, layout_id, is_archived)
VALUES 
(1, 4, 1, 0),
(2, 6, 1, 0),
(3, 2, 2, 0),
(4, 8, 2, 0),
(5, 2, 3, 0),
(6, 4, 3, 0),
(7, 6, 3, 0),
(8, 2, 4, 0),
(9, 4, 4, 0),
(10, 10, 4, 0);

-- Events
INSERT INTO event (id, created_date, description, end_date, is_archived, name, price, start_date, updated_date, layout_id, menu_id)
VALUES 
(1, NOW(), 'Spring Launch Event with live music', '2024-05-10', 0, 'Spring Launch', 0, '2024-05-01', NOW(), 1, 1),
(2, NOW(), 'Summer Fiesta featuring grilled delights', '2025-07-15', 0, 'Summer Fiesta', 59.99, '2025-07-01', NOW(), 2, 2),
(3, NOW(), 'Kids Day Out', '2025-06-10', 0, 'Kids Day', 19.99, '2025-06-01', NOW(), 1, 3),
(4, NOW(), 'A classy wine tasting experience with sommelier insights', '2025-08-10', 0, 'Wine Tasting Night', 69.99, '2025-08-01', NOW(), 3, 1),
(5, NOW(), 'Evening rooftop BBQ party with live DJ', '2025-08-20', 0, 'Rooftop BBQ Bash', 39.99, '2025-08-15', NOW(), 4, 2),
(6, NOW(), 'Live Jazz Night with candlelight dining', '2025-09-10', 0, 'Jazz Night', 54.99, '2025-09-01', NOW(), 3, 1);

-- Seatings
INSERT INTO seating (id, created_date, duration_in_minutes, seating_date_time, updated_date, event_id)
VALUES 
(1, NOW(), 90, '2024-05-05 18:00:00', NOW(), 1),
(2, NOW(), 120, '2025-07-05 19:30:00', NOW(), 2),
(3, NOW(), 60, '2025-06-05 11:00:00', NOW(), 3),
(4, NOW(), 75, '2025-08-02 18:30:00', NOW(), 4),
(5, NOW(), 90, '2025-08-16 20:00:00', NOW(), 5),
(6, NOW(), 105, '2025-09-02 19:00:00', NOW(), 6),
(7, NOW(), 60, '2024-05-06 12:00:00', NOW(), 1),
(8, NOW(), 120, '2025-07-06 21:00:00', NOW(), 2),
(9, NOW(), 45, '2025-06-06 10:30:00', NOW(), 3);

-- Reservations
INSERT INTO reservation (id, email, first_name, group_size, last_name, status, seating_id, dining_table_id)
VALUES 
(1, 'guest1@example.com', 'Aaron', 4, 'Brown', 'APPROVED', 1, 1),
(2, 'guest2@example.com', 'Bob', 2, 'Green', 'PENDING', 2, 3),
(3, 'guest3@example.com', 'Nick', 6, 'White', 'DENIED', 1, 2),
(4, 'guest4@example.com', 'Diana', 3, 'Black', 'APPROVED', 3, 4);

-- Menu Items
INSERT INTO menu_item (id, created_date, description, name, menu_id)
VALUES 
(1, NOW(), 'Grilled salmon with lemon butter sauce', 'Grilled Salmon', 1),
(2, NOW(), 'Strawberry spinach salad with vinaigrette', 'Strawberry Salad', 1),
(3, NOW(), 'BBQ ribs with corn on the cob', 'BBQ Ribs', 2),
(4, NOW(), 'Chilled mango smoothie', 'Mango Smoothie', 2),
(5, NOW(), 'Mini burgers with fries', 'Kids Mini Burger', 3),
(6, NOW(), 'Mac & cheese bowl', 'Mac & Cheese', 3);
