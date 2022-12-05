package edu.itstep.hw20221119;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import edu.itstep.hw20221119.models.ConstantsStore;
import edu.itstep.hw20221119.models.Order;
import edu.itstep.hw20221119.models.PizzaRecipe;
import edu.itstep.hw20221119.models.PizzaSize;
import edu.itstep.hw20221119.models.PizzaTopping;
import edu.itstep.hw20221119.models.ToppingCount;

public class OrderActivity extends AppCompatActivity {

    // поля - змінні класу, що відповідають активним елементам Activity
    private TextView tvPizzaRecipe;
    private TextView tvPizzaSize;
    private TextView tvPizzaAmount;
    private TextView tvTopping;
    private TextView tvPrice;

    private EditText etPersonName;
    private EditText etPhone;

    private Button btnOrder;

    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        initView(); // ініціалізація даних
        setListener(); // підключення слухачів
        initData(); // ініціалізація первинних даних
    }

    // ініціалізація змінних класу
    private void initView() {
        tvPizzaRecipe = findViewById(R.id.tvPizzaRecipe);
        tvPizzaSize = findViewById(R.id.tvPizzaSize);
        tvPizzaAmount = findViewById(R.id.tvPizzaAmount);
        tvTopping = findViewById(R.id.tvTopping);
        tvPrice = findViewById(R.id.tvPrice);

        etPersonName = findViewById(R.id.etPersonName);
        etPhone = findViewById(R.id.etPhone);

        btnOrder = findViewById(R.id.btnOrder);
    }

    // підключення слухачів/подій
    private void setListener() {
        Resources resources = getResources();
        btnOrder.setOnClickListener(view -> {
            if (etPersonName.getText().toString().trim().length() == 0 || etPhone.getText().toString().trim().length() == 0) {
                String message = resources.getString(R.string.messageInputYourData);
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            } else {
                String message = resources.getString(R.string.messageOrder);
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ініціалізація даних Актівіті з використанням даних, отриманих в моделі
    private void initData() {
        // отримання об'єкта Intent, відправленого з попереднього актівіті -
        Intent intent = getIntent();

        // отримання даних з об'єкта Intent з використанням методу, getSerializableExtra -
        // використовується у разі передачі даних через модель, що імплементує інтерфейс Serializable
        order = (Order) intent.getSerializableExtra(ConstantsStore.KEY_ORDER);

        // отримання об'єкта Resources, що дозволяє звертатись до різних елементі проекту
        Resources resources = getResources();

        setTvPizzaRecipe(resources);
        setTvPizzaSize(resources);
        setTvPizzaAmount();
        setTvToppings(resources);
        setTvPrice(resources);
    }

    // розрахунок вартості замовлення
    private void setTvPrice(Resources resources) {
        double priceToppings = 0;
        for (ToppingCount toppingCount : order.getToppingCountList()) {
            priceToppings += toppingCount.getPizzaTopping().getCost() * toppingCount.getCount();
        }

        double pizzaPriceWithToppings = order.getPizzaRecipe().getCost() + priceToppings;

        double price = (pizzaPriceWithToppings + (pizzaPriceWithToppings * order.getPizzaSize().getMargin() / 100)) * order.getPizzaCount();

        String newPrice = String.valueOf(price) + resources.getString(R.string.currencyType);
        tvPrice.setText(newPrice);
    }

    // ініціалізація поля, що відображає добавки та їх кількість
    private void setTvToppings(Resources resources) {
        if (order.getToppingCountList().size() > 0) {
            for (ToppingCount toppingCount : order.getToppingCountList()) {
                String newTvToppingText = tvTopping.getText() +
                        (tvTopping.getText().length() > 0 ? "\n" : "") +
                        toppingSelection(resources, toppingCount);
                tvTopping.setText(newTvToppingText);
            }
        }
    }

    // повертає рядок, що складається з назви добавки та її кількості
    private String toppingSelection(Resources resources, ToppingCount toppingCount) {
        if (PizzaTopping.MEAT == toppingCount.getPizzaTopping()) {
            return resources.getString(R.string.tvMeat) + " - " + toppingCount.getCount();
        } else if (PizzaTopping.MUSHROOMS == toppingCount.getPizzaTopping()) {
            return resources.getString(R.string.tvMushroom) + " - " + toppingCount.getCount();
        }
        return resources.getString(R.string.tvCheese) + " - " + toppingCount.getCount();
    }

    // ініціалізація поля, що відображає кіькість замовлення піци
    private void setTvPizzaAmount() {
        tvPizzaAmount.setText(String.valueOf(order.getPizzaCount()));
    }

    // ініціалізація поля, що відображає розмір піци
    private void setTvPizzaSize(Resources resources) {
        if (PizzaSize.LARGE == order.getPizzaSize()) {
            tvPizzaSize.setText(resources.getString(R.string.rBtnLarge));
        } else if (PizzaSize.MEDIUM == order.getPizzaSize()) {
            tvPizzaSize.setText(resources.getString(R.string.rBtnMedium));
        } else if (PizzaSize.SMALL == order.getPizzaSize()) {
            tvPizzaSize.setText(resources.getString(R.string.rBtnSmall));
        }
    }

    // ініціалізація поля, що відображає обраний рецепт піци
    private void setTvPizzaRecipe(Resources resources) {
        if (PizzaRecipe.PANCHETA_GORGONDZOLA == order.getPizzaRecipe()) {
            tvPizzaRecipe.setText(resources.getString(R.string.tvPizza_1));
        } else if (PizzaRecipe.MEAT_PIZZA == order.getPizzaRecipe()) {
            tvPizzaRecipe.setText(resources.getString(R.string.tvPizza_2));
        } else if (PizzaRecipe.PHILADELPHIA == order.getPizzaRecipe()) {
            tvPizzaRecipe.setText(resources.getString(R.string.tvPizza_3));
        }
    }

}