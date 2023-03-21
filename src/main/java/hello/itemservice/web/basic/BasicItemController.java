package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor // final 붙은 필드 생성자주입 알아서해줌
public class BasicItemController {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items"; // 템플릿 / 베이직 / items로 이동
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }

//    @PostMapping("/add")
    public String addItemV1(@RequestParam String itemName,@RequestParam int price,@RequestParam Integer quantity, Model model) {

        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);

        model.addAttribute("item", item);
        return "basic/item";
    }

//    @PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item,Model model) {

        /* 위에 Item 인스턴스화해서 set한거를 ModelAttribute가 알아서 다 해줌 ( 요청 파라미터를 프로퍼티 접근법으로 -> 객체와 이름이 맞아야함 )
        * 아래 주석한 addAttribute도 @ModelAttribue ( ) 속성에 item 네임 설정하면 자동 추가 되므로 생략 가능함. */

        itemRepository.save(item);

//        model.addAttribute("item", item); // model에 들어가는 거를 위에 ModelAttribute 속성에 넣으면 생략 가능
        return "basic/item";
    }

//    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item,Model model) {

        /* ModelAttribute 속성 제외하면 참조타입객체를 소문자로 바꿈 Item -> item 그 후 model.attribute 자동 추가됨 */

        itemRepository.save(item);
        return "basic/item";
    }

//    @PostMapping("/add")
    public String addItemV4(Item item) {

        /* String,int 같은 단순타입 이면 @RequestParam , 아니면 @ModelAttribute 알아서 붙음  */

        itemRepository.save(item);
        return "basic/item";
    }

//    @PostMapping("/add")
    public String addItemV5(Item item) {
        itemRepository.save(item);
        return "redirect:/basic/items/" + item.getId();

    }

    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes) {
        Item saveItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", saveItem.getId()); // 아래 return PathVariable부분에 자동으로 넣어줌
        redirectAttributes.addAttribute("status", true);

        return "redirect:/basic/items/{itemId}";

    }


    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId,item);
        return "redirect:/basic/items/{itemId}";
    }


    /**
     *  테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item(("itemA"), 10000, 10));
        itemRepository.save(new Item(("itemB"), 20000, 20));
    }
}
