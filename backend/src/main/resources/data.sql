INSERT INTO hostels (name, location, total_rooms)
SELECT 'Hostel A', 'Main Campus - Block A', 0
WHERE NOT EXISTS (SELECT 1 FROM hostels WHERE name = 'Hostel A');

INSERT INTO hostels (name, location, total_rooms)
SELECT 'Hostel B', 'Main Campus - Block B', 0
WHERE NOT EXISTS (SELECT 1 FROM hostels WHERE name = 'Hostel B');
