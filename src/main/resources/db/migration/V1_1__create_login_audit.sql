CREATE TABLE IF NOT EXISTS login_audit (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(191) NOT NULL,
  success  TINYINT(1)   NOT NULL,
  ip_address VARCHAR(45),
  user_agent VARCHAR(512),
  login_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_login_audit_user_time (username, login_at),
  INDEX idx_login_audit_time (login_at)
);