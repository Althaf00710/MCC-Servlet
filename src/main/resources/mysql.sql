CREATE TABLE User (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      username VARCHAR(50) NOT NULL UNIQUE,
                      password VARCHAR(100) NOT NULL,
                      firstName VARCHAR(50) NOT NULL,
                      lastName VARCHAR(50) NOT NULL,
                      email VARCHAR(100) NOT NULL UNIQUE,
                      phoneNumber VARCHAR(15) NOT NULL UNIQUE,
                      avatarUrl VARCHAR(250) NULL,
                      lastActive VARCHAR(255) NOT NULL DEFAULT 'Never'
);

SELECT * FROM User;

CREATE TABLE Customer (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          registerNumber VARCHAR(50),
                          name VARCHAR(100),
                          address TEXT,
                          phoneNumber VARCHAR(15),
                          email VARCHAR(100),
                          nicNumber VARCHAR(20)
);

CREATE TABLE Driver (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(100),
                        nicNumber VARCHAR(20),
                        licenceNumber VARCHAR(50),
                        phoneNumber VARCHAR(15),
                        email VARCHAR(100),
                        avatarUrl TEXT
);

CREATE TABLE CabBrand (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          brandName VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE CabType (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         typeName VARCHAR(50) NOT NULL UNIQUE,
                         description TEXT,
                         capacity INT NOT NULL,
                         baseFare DOUBLE NOT NULL,
                         baseWaitTimeFare DOUBLE NOT NULL
);

CREATE TABLE Cab (
                     id INT AUTO_INCREMENT PRIMARY KEY,
                     cabBrandId INT NOT NULL,
                     cabTypeId INT NOT NULL,
                     cabName VARCHAR(100),
                     registrationNumber VARCHAR(50),
                     plateNumber VARCHAR(50),
                     status VARCHAR(20),
                     lastService DATE,
                     FOREIGN KEY (cabBrandId) REFERENCES CabBrand(id) ON DELETE CASCADE,
                     FOREIGN KEY (cabTypeId) REFERENCES CabType(id) ON DELETE CASCADE
);

CREATE TABLE CabAssign (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           cabId INT NOT NULL,
                           driverId INT NOT NULL,
                           assignDate DATE NOT NULL,
                           FOREIGN KEY (cabId) REFERENCES Cab(id) ON DELETE CASCADE,
                           FOREIGN KEY (driverId) REFERENCES Driver(id) ON DELETE CASCADE
);

CREATE TABLE Booking (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         bookingNumber INT NOT NULL UNIQUE,
                         cabId INT NOT NULL,
                         customerId INT NOT NULL,
                         userId INT NOT NULL,
                         bookingDateTime DATETIME NOT NULL,
                         dateTimeCreated DATETIME DEFAULT CURRENT_TIMESTAMP,
                         status VARCHAR(20) NOT NULL,
                         pickupLocation TEXT,
                         longitude VARCHAR(50),
                         latitude VARCHAR(50),
                         FOREIGN KEY (cabId) REFERENCES Cab(id) ON DELETE CASCADE,
                         FOREIGN KEY (customerId) REFERENCES Customer(id) ON DELETE CASCADE,
                         FOREIGN KEY (userId) REFERENCES User(id) ON DELETE CASCADE
);

CREATE TABLE Stop (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      bookingId INT NOT NULL,
                      stopLocation TEXT,
                      longitude VARCHAR(50),
                      latitude VARCHAR(50),
                      distanceFromLastStop DOUBLE NOT NULL,
                      waitMinutes INT NOT NULL,
                      FOREIGN KEY (bookingId) REFERENCES Booking(id) ON DELETE CASCADE
);

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
