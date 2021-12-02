import com.ktscrap.model.StockGpw
import com.ktscrap.repository.GpwRepository
import org.assertj.core.api.Assertions.assertThat

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
class StockGpwScrapTest(@Autowired val repo: GpwRepository) {
    @Test
    fun givenStock_whenSave(){
        val stockGpwToSave = StockGpw(1,"euro" ,"3.50","2","3","4","1414","4444" )
        val hashCodeBefore = stockGpwToSave.hashCode();
        val personSet = hashSetOf(hashCodeBefore)
        repo.save(stockGpwToSave)
        val hashAfter = stockGpwToSave.hashCode()
        assertThat(repo.findAll()).hasSize(1)
        assertThat(personSet).contains(stockGpwToSave.hashCode())
        assertThat(hashAfter).isEqualTo(hashCodeBefore)
    }
}