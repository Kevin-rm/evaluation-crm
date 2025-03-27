package site.easy.to.build.crm;

import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.util.CsvUtil;
import site.easy.to.build.crm.util.DatabaseCustomUtil;

import java.io.IOException;
import java.sql.SQLDataException;

@SpringBootTest
class CrmApplicationTests {

	@Autowired
	private LeadService leadService;

	@Test
	void contextLoads() throws IOException, SQLDataException {
		Lead lead = leadService.findByLeadId(78);
		Hibernate.initialize(lead.getCustomer());

		System.out.println(lead.getCustomer());
	}

}
