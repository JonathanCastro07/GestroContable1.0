package com.Proyecto.Gestor_Contable.Repository;

import com.Proyecto.Gestor_Contable.Modelo.Origen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrigenRepository extends JpaRepository<Origen, Long> {
}
