package edu.pe.cibertec.cl3_caceres;

import java.util.List;
import org.springframework.data.repository.CrudRepository;


public interface EmpleadoRepository extends CrudRepository <Empleado, Integer> {
    List<Empleado> findAll(); 
    
}