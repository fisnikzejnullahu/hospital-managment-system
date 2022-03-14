/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reports;

import reports.modeljrxml.SemundjaFactory;
import exceptions.SpitaliDbException;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import model.tablemodels.PunetoriTableModel;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.swing.JRViewer;
import reports.modeljrxml.Item;
/**
 *
 * @author Fisnik Zejnullahu
 */
public class ReportGenerator extends JFrame{

    private static final long serialVersionUID = 1L;
    
    private PunetoriTableModel punetori;
    private JasperDesign jd;
    private JasperReport jr;
    private JasperPrint jp;
    
    public ReportGenerator(){
    }
    
    public File exportReportToPdf(String fileName) throws JRException, SQLException{
        String userhome = System.getProperty("user.home");
        JasperExportManager.exportReportToPdfFile(jp, userhome +"/downloads/"+fileName);
        return new File(userhome +"/downloads");
    }
    
    public void printReport() throws JRException{
        JasperPrintManager.printReport(jp, true);
    }
    
    public void exportPunetoriReport() throws JRException, SpitaliDbException, SQLException{
        Map<String, Object> param = new HashMap<>();
        param.put("id_param", String.valueOf(punetori.getId()));
        param.put("nrpersonal_param", String.valueOf(punetori.getNrPersonal()));
        param.put("emri_param", punetori.getEmri());
        param.put("emriBabes_param", punetori.getEmriBabes());
        param.put("mbiemri_param", punetori.getMbiemri());
        param.put("dep_param", punetori.getDepartamenti());
        param.put("nrtel_param", punetori.getNrTel());
        param.put("adresa_param", punetori.getAdresa());
        param.put("email_param", punetori.getEmail());
        param.put("gjinia_param", punetori.getGjinia());
        param.put("datelindja_param", punetori.getDatelindja());
        param.put("dataFillimit_param", punetori.getDataFillimit());
        param.put("dataMbarimit_param", punetori.getDataMbarimit());
        
        jd = JRXmlLoader.load("C:\\Users\\Xhevat Zejnullahu\\Documents\\NetBeansProjects\\HealthCare System 2018\\src\\reports\\modeljrxml\\detajetPunetore.jrxml");
        jr = JasperCompileManager.compileReport(jd);

        ArrayList<PunetoriTableModel> lista = new ArrayList<>();
        lista.add(punetori);
        JRBeanCollectionDataSource beanColDataSource =
            new JRBeanCollectionDataSource(lista);
        
        jp = JasperFillManager.fillReport(jr, param, beanColDataSource);
        this.add(new JRViewer(jp));
        this.setSize(1400, 860);
    }
    
    public void setPunetori(PunetoriTableModel p){
        punetori = p;
    }
    
    public void showReport(){
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    
    public void exportSemundjetPacientitReport(List<Item> lista, String emriPacientit) throws JRException, SpitaliDbException, SQLException{
        
//        List<Item> items = new ArrayList<>();
//        for (String[] row : lista){
//            items.add(new Item(row[0], row[1], row[2]));
//        }

        jd = JRXmlLoader.load("C:\\Users\\Xhevat Zejnullahu\\Documents\\NetBeansProjects\\HealthCare System 2018\\src\\reports\\modeljrxml\\semundjetPacientit.jrxml");
        jr = JasperCompileManager.compileReport(jd);

        SemundjaFactory.semundjetList = lista;
        
        Map<String, Object> param = new HashMap<>();
        param.put("emriPac_param", emriPacientit);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(SemundjaFactory.semundjetList);
        
        jp = JasperFillManager.fillReport(jr, param, dataSource);
        this.add(new JRViewer(jp));
        this.setSize(1500, 920);
    }
}
