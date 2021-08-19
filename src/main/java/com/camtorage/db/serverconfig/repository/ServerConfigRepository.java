package com.camtorage.db.serverconfig.repository;

import com.camtorage.entity.serverconfig.ServerConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServerConfigRepository extends JpaRepository<ServerConfig, Integer> {
}
