package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.datajpa.entity.Item;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Test
    public void save(){
//  1. id GenerateValeu <- JPA perist를 하면 기본값 Setting
//        Item item = new Item();
//  2. pk값이 있다면? perist가 호출되지않음. 기존 entity가 있다고 생각하고 merge를 하게됨. -> DB에 A에 있을꺼라고 가정,,
//  merge를 기본적으로 쓰지않는걸,, 가정,, 성능 비효율,, 될 수 있으면, GenerateVale를 사용,,, 그럼,,

//  3. Persistable을 사용
//  Auditing ex) @CreateData 를 활용해서 isNew의 Ovverride를 통해, 확인여부를 직접구현,
        Item item = new Item("A");
        itemRepository.save(item);
    }
}