package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.deals.Deal;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional
@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class DealJpaDaoTest {

    private static final long EXISTING_BOOK_ID = 102;

    private static final long EXISTING_ID = 101;

    @Autowired
    private DealJpaDao dealDao;

    @PersistenceContext
    private EntityManager em;



    @Test
    public void testCreate() {

        Deal deal = dealDao.create(EXISTING_BOOK_ID, BigDecimal.valueOf(0.1), LocalDate.now(), LocalDate.now().plusDays(21));

        Assert.assertEquals(EXISTING_BOOK_ID, deal.getDealId());
        Assert.assertEquals(BigDecimal.valueOf(0.1), deal.getPrice());
        Assert.assertEquals(1, TestUtils.getRowCount(em, "FROM deals WHERE id = 102 AND price = 0.1"));
    }

    @Test
    public void testUpdate() {

        Deal deal = em.find(Deal.class, EXISTING_ID);

        dealDao.update(deal, BigDecimal.ONE, LocalDate.now());

        Assert.assertEquals(1, TestUtils.getRowCount(em, "FROM deals WHERE id = 101 AND price = 1"));
    }

    @Test
    public void testFindByIdExisting(){
        Optional<Deal> maybeDeal = dealDao.findById(EXISTING_ID);

        Assert.assertNotNull(maybeDeal);
        Assert.assertTrue(maybeDeal.isPresent());
    }

    @Test
    public void testFindByIdNonExisting(){
        Optional<Deal> maybeDeal = dealDao.findById(999999);

        Assert.assertNotNull(maybeDeal);
        Assert.assertTrue(maybeDeal.isEmpty());
    }

    @Test
    public void testDeleteNonExisting() {

        dealDao.deleteDeal(99999);

        Assert.assertEquals(0, TestUtils.getRowCount(em, "FROM deals WHERE id = 99999"));
    }


    @Test
    public void testDeleteExisting() {

        dealDao.deleteDeal(EXISTING_ID);

        Assert.assertEquals(0, TestUtils.getRowCount(em, "FROM deals WHERE id = 101"));
    }

    @Test
    public void testGetAll(){
        List<Deal> deals = dealDao.getAll();

        Assert.assertNotNull(deals);
        Assert.assertEquals(TestUtils.getRowCount(em, "FROM deals"), deals.size());
    }
}
