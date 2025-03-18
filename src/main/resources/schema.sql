CREATE TABLE IF NOT EXISTS domain_events (
  id CHAR(36) NOT NULL,
  aggregate_id CHAR(36) NOT NULL,
  name VARCHAR(255) NOT NULL,
  body LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL CHECK (json_valid(body)),
  occurred_on TIMESTAMP NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
