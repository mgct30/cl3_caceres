package edu.pe.cibertec.cl3_caceres;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.data.DataSourceCollection;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;



@Controller
@RequestMapping("empleados")
public class EmpleadoController {
    
    EmpleadoRepository empleadoRepository;
    DataSource dataSource;
    
    public EmpleadoController(EmpleadoRepository empleadoRepository, DataSource dataSource){
        this.empleadoRepository = empleadoRepository;
        this.dataSource = dataSource;
    }

    @GetMapping
    public String list(Model modelo){
        List<Empleado> empleados = empleadoRepository.findAll();
        modelo.addAttribute("listaEmpleados", empleados);

        return "empleados/list";
   }

   @GetMapping("create")
   public String showCreateForm(Model model) {
       EmpleadoDto empleadoDto = new EmpleadoDto();
       model.addAttribute("empleadoForm", empleadoDto);
       return "empleados/create";
   }

   @PostMapping
    public String create(EmpleadoDto empleadoDto) {
    
    Empleado empleado = new Empleado(
        empleadoDto.getNombre(),
        empleadoDto.getApellido(),
        empleadoDto.getSalario(),
        empleadoDto.getPuesto()
    );
    empleadoRepository.save(empleado);
    return "redirect:/empleados";
    
    }


    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        Optional<Empleado> empleadoOptional = empleadoRepository.findById(id);
        if (empleadoOptional.isEmpty()) {
            return "404";
        }

        Empleado empleado = empleadoOptional.get();
        model.addAttribute("empleado", empleado);
        return "empleados/detail";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Optional<Empleado> empleadoOptional = empleadoRepository.findById(id);
        if (empleadoOptional.isEmpty()) {
            return "404"; 
        }
        Empleado empleado = empleadoOptional.get();
        model.addAttribute("empleado", empleado);
        return "empleados/edit"; 
    }  
    

    @PostMapping("/{id}")
    public String edit(@PathVariable Integer id, Empleado empleadoDataForm) {
        Optional<Empleado> empleadoOptional = empleadoRepository.findById(id);
        if (empleadoOptional.isEmpty()) {
            return "404"; 
        }
        Empleado empleado = empleadoOptional.get();
        empleado.setNombre(empleadoDataForm.getNombre());
        empleado.setApellido(empleadoDataForm.getApellido());
        empleado.setSalario(empleadoDataForm.getSalario());
        empleado.setPuesto(empleadoDataForm.getPuesto());
        empleadoRepository.save(empleado);

        return "redirect:/empleados";
    } 
    
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id) {
        empleadoRepository.deleteById(id);
        return "redirect:/empleados"; 

    }

    @GetMapping("reporte")
    public void downloadReport(HttpServletResponse response) throws SQLException {
        try {
            InputStream inputStream = new ClassPathResource("reports/listado_productos.jasper").getInputStream();
            JasperReport report = (JasperReport) JRLoader.loadObject(inputStream);

            // JRDataSource dataSource = new JREmptyDataSource();
            Connection connection = dataSource.getConnection();


            Map<String, Object> parameters = new HashMap<>();
        

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, connection);
            connection.close();

            // OutputStream outputStream = new FileOutputStream("hola.pdf");
            response.setContentType("application/pdf");
            OutputStream outputStream =  response.getOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

        } catch (IOException | JRException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}   