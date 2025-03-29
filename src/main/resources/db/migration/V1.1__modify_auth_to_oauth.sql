# Date :            2025-03-29
# Author :          강성욱
# Description :     auth 에서 oauth 로 이름 전체 변경

-- Rename table auth to oauth
ALTER TABLE auth RENAME TO oauth;

-- Rename table auth_provider to oauth_provider
ALTER TABLE auth_provider RENAME TO oauth_provider;
