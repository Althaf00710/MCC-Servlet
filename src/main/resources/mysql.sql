CREATE TABLE User (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      username VARCHAR(50) NOT NULL UNIQUE,
                      password VARCHAR(100) NOT NULL,
                      firstName VARCHAR(50) NOT NULL,
                      lastName VARCHAR(50) NOT NULL,
                      email VARCHAR(100) NOT NULL UNIQUE,
                      countryCode VARCHAR(5) NOT NULL DEFAULT '+94',
                      phoneNumber VARCHAR(15) NOT NULL UNIQUE,
                      avatarUrl VARCHAR(250) NULL,
                      lastActive VARCHAR(255) NOT NULL DEFAULT 'Never'
);

CREATE TABLE Customer (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          registerNumber VARCHAR(50),
                          name VARCHAR(100),
                          address TEXT,
                          countryCode VARCHAR(5),
                          phoneNumber VARCHAR(15),
                          email VARCHAR(100),
                          nicNumber VARCHAR(20),
                          joinedDate VARCHAR(50)
);

SELECT * FROM customer;

CREATE TABLE Driver (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(100),
                        nicNumber VARCHAR(20),
                        licenceNumber VARCHAR(50),
                        phoneNumber VARCHAR(15),
                        email VARCHAR(100),
                        avatarUrl TEXT,
                        status VARCHAR(20) NOT NULL DEFAULT 'Never'
);

SELECT * FROM driver;

CREATE TABLE CabBrand (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          brandName VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE CabType (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         typeName VARCHAR(50) NOT NULL UNIQUE,
                         imageUrl TEXT,
                         capacity INT NOT NULL,
                         baseFare DOUBLE NOT NULL,
                         baseWaitTimeFare DOUBLE NOT NULL
);

SELECT * FROM cabtype;

CREATE TABLE Cab (
                     id INT AUTO_INCREMENT PRIMARY KEY,
                     cabBrandId INT NOT NULL,
                     cabTypeId INT NOT NULL,
                     cabName VARCHAR(100),
                     registrationNumber VARCHAR(50),
                     plateNumber VARCHAR(50),
                     status VARCHAR(20) DEFAULT 'Available',
                     lastService DATETIME DEFAULT CURRENT_TIMESTAMP,
                     FOREIGN KEY (cabBrandId) REFERENCES CabBrand(id) ON DELETE CASCADE,
                     FOREIGN KEY (cabTypeId) REFERENCES CabType(id) ON DELETE CASCADE
);

SELECT c.id, c.cabBrandId, c.cabTypeId,
       CONCAT(b.brandName, ' ', c.cabName) AS cabFullName,
       c.registrationNumber, c.plateNumber, c.status, c.lastService
FROM Cab c
         JOIN CabBrand b ON c.cabBrandId = b.id
WHERE c.cabTypeId = 1 AND c.status = "Available";


SELECT * FROM cab;

CREATE TABLE CabAssign (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           cabId INT NOT NULL,
                           driverId INT NOT NULL,
                           assignDate DATE DEFAULT CURRENT_TIMESTAMP,
                           status VARCHAR(25) NOT NULL,
                           FOREIGN KEY (cabId) REFERENCES Cab(id) ON DELETE CASCADE,
                           FOREIGN KEY (driverId) REFERENCES Driver(id) ON DELETE CASCADE
);

SELECT * FROM CabAssign;

SELECT * FROM driver;

CREATE TABLE Booking (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         bookingNumber VARCHAR(25) NOT NULL UNIQUE,
                         cabId INT NOT NULL,
                         customerId INT NOT NULL,
                         userId INT,
                         bookingDateTime DATETIME NOT NULL,
                         dateTimeCreated DATETIME DEFAULT CURRENT_TIMESTAMP,
                         status VARCHAR(20) NOT NULL,
                         pickupLocation TEXT,
                         longitude DECIMAL(10, 6),
                         latitude DECIMAL(10, 6),
                         placeId VARCHAR(100),
                         FOREIGN KEY (cabId) REFERENCES Cab(id) ON DELETE CASCADE,
                         FOREIGN KEY (customerId) REFERENCES Customer(id) ON DELETE CASCADE,
                         FOREIGN KEY (userId) REFERENCES User(id) ON DELETE CASCADE
);

CREATE TABLE Stop (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      bookingId INT NOT NULL,
                      stopLocation TEXT,
                      longitude DECIMAL(10, 6),
                      latitude DECIMAL(10, 6),
                      placeId VARCHAR(100),
                      distanceFromLastStop DOUBLE NOT NULL,
                      waitMinutes INT NOT NULL,
                      FOREIGN KEY (bookingId) REFERENCES Booking(id) ON DELETE CASCADE
);

SELECT * FROM booking;
SELECT * FROM stop;

CREATE TABLE Billing (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         bookingId INT NOT NULL,
                         totalDistanceFare DOUBLE NOT NULL,
                         totalWaitFare DOUBLE NOT NULL,
                         billDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         tax DOUBLE NOT NULL,
                         discount DOUBLE NOT NULL,
                         userId INT NOT NULL,
                         cash DOUBLE,
                         deposit DOUBLE,
                         card DOUBLE,
                         totalAmount DOUBLE GENERATED ALWAYS AS (
                             ((totalDistanceFare + totalWaitFare) * (1 - discount / 100)) * (1 + tax / 100)
                             ) STORED,
                         FOREIGN KEY (bookingId) REFERENCES Booking(id) ON DELETE CASCADE,
                         FOREIGN KEY (userId) REFERENCES User(id) ON DELETE CASCADE
);

CREATE TABLE Review (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        bookingId INT NOT NULL,
                        star INT NOT NULL CHECK (star BETWEEN 1 AND 5),
                        review TEXT,
                        approved BOOLEAN DEFAULT FALSE,
                        FOREIGN KEY (bookingId) REFERENCES Booking(id) ON DELETE CASCADE
);

CREATE TABLE CompanyData (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             address TEXT NOT NULL,
                             phoneNumber VARCHAR(15) NOT NULL,
                             email VARCHAR(100) NOT NULL,
                             tax DOUBLE NOT NULL,
                             discount DOUBLE NOT NULL,
                             minAmountForDiscount DOUBLE NOT NULL
);

SELECT * FROM companydata;

SELECT
    b.id AS bookingId,
    b.bookingNumber,
    b.cabId,
    CONCAT(cb.brandName, ' ', c.cabName) AS cabName,
    b.customerId,
    cu.name AS customerName,
    b.userId,
    u.username AS userName,
    b.bookingDateTime,
    b.dateTimeCreated,
    b.status,
    b.pickupLocation,
    b.longitude AS pickupLongitude,
    b.latitude AS pickupLatitude,
    b.placeId AS pickupPlaceId
FROM Booking b
         LEFT JOIN Cab c ON b.cabId = c.id
         LEFT JOIN CabBrand cb ON c.cabBrandId = cb.id
         LEFT JOIN Customer cu ON b.customerId = cu.id
         LEFT JOIN User u ON b.userId = u.id
ORDER BY b.id DESC;
