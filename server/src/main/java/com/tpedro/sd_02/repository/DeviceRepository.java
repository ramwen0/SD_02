package com.tpedro.sd_02.repository;

import com.tpedro.sd_02.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<Device, String> {
	// Interface para operações CRUD em dispositivos
	long countByAtivoTrue();
}
