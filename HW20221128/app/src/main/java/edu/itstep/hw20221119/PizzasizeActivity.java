package edu.itstep.hw20221119;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import edu.itstep.hw20221119.models.ConstantsStore;
import edu.itstep.hw20221119.models.Order;
import edu.itstep.hw20221119.models.PizzaSize;
import edu.itstep.hw20221119.models.PizzaTopping;
import edu.itstep.hw20221119.models.ToppingCount;

public class PizzasizeActivity extends AppCompatActivity implements View.OnClickListener {

    // поля - змінні класу, що відповідають активним елементам Activity
    private RadioGroup rgPizzaSize;

    private ImageView ivMinusMeat;
    private ImageView ivPlusMeat;
    private ImageView ivMinusMushroom;
    private ImageView ivPlusMushroom;
    private ImageView ivMinusCheese;
    private ImageView ivPlusCheese;
    private TextView tvCountMeat;
    private TextView tvCountMushroom;
    private TextView tvCountCheese;

    private Button btnMakeOrder;

    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pizzasize);

        initView(); // ініціалізація даних
        setListener(); // підключення слухачів
        initData(); // ініціалізація первинних даних
        dataLoadAfterScreenRotation(savedInstanceState); // завантаження даних після зміни положення екрана
    }

    // збереження даних при зміні положення екрана
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // відправка моделі Order до об'єкта Bundle
        // використовується метод .putSerializable() оскільки Order імплементує інтерфейс Serializable
        outState.putSerializable(ConstantsStore.KEY_ORDER, order);
    }

    // завантаження даних після зміни положення екрана
    private void dataLoadAfterScreenRotation(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // отримання даних - моделі Order з об'єкта Bundle
            // використовується метод .getSerializable() оскільки Order імплементує інтерфейс Serializable
            // в параметрах вказується ключ, наданий при збереженні даних до об'єкта Bundle
            order = (Order) savedInstanceState.getSerializable(ConstantsStore.KEY_ORDER);

            // оновлення стану активних елементів Актівіті після зміни положення екрану
            setDataOnScreen();
        }
    }

    // оновлення стану активних елементів Актівіті після зміни положенні екрану
    private void setDataOnScreen() {
        // елемент радіобатон (розмір піци) підтримує збереження стану після зміни положення

        // елементи TextView - кількість добавок
        if (order.getToppingCountList().size() > 0) {
            for (ToppingCount toppingCount : order.getToppingCountList()) {
                if(toppingCount.getPizzaTopping() == PizzaTopping.MEAT){
                    tvCountMeat.setText(String.valueOf(toppingCount.getCount()));
                } else if(toppingCount.getPizzaTopping() == PizzaTopping.MUSHROOMS){
                    tvCountMushroom.setText(String.valueOf(toppingCount.getCount()));
                } else if(toppingCount.getPizzaTopping() == PizzaTopping.CHEESE){
                    tvCountCheese.setText(String.valueOf(toppingCount.getCount()));
                }
            }
        }
    }

    // ініціалізація змінних класу
    private void initView() {
        rgPizzaSize = findViewById(R.id.rgPizzaSize);

        ivMinusMeat = findViewById(R.id.ivMinusMeat);
        ivPlusMeat = findViewById(R.id.ivPlusMeat);
        ivMinusMushroom = findViewById(R.id.ivMinusMushroom);
        ivPlusMushroom = findViewById(R.id.ivPlusMushroom);
        ivMinusCheese = findViewById(R.id.ivMinusCheese);
        ivPlusCheese = findViewById(R.id.ivPlusCheese);
        tvCountMeat = findViewById(R.id.tvCountPizza);
        tvCountMushroom = findViewById(R.id.tvCountMushroom);
        tvCountCheese = findViewById(R.id.tvCountCheese);

        btnMakeOrder = findViewById(R.id.btnMakeOrder);
    }

    // підключення слухачів/подій
    private void setListener() {
        rgPizzaSize.setOnCheckedChangeListener(this::setPizzaSize);

        ivMinusMeat.setOnClickListener(this);
        ivPlusMeat.setOnClickListener(this);
        ivMinusMushroom.setOnClickListener(this);
        ivPlusMushroom.setOnClickListener(this);
        ivMinusCheese.setOnClickListener(this);
        ivPlusCheese.setOnClickListener(this);

        btnMakeOrder.setOnClickListener(this);
    }

    // вибір розміру піци - обробка радіо-групи (користувач обирає радіобатони)
    private void setPizzaSize(RadioGroup radioGroup, int id) {
        if (id == R.id.rBtnLarge) {
            order.setPizzaSize(PizzaSize.LARGE);
        } else if (id == R.id.rBtnMedium) {
            order.setPizzaSize(PizzaSize.MEDIUM);
        } else if (id == R.id.rBtnSmall) {
            order.setPizzaSize(PizzaSize.SMALL);
        }
    }

    // ініціалізація даних, отримання даних з моделі
    private void initData() {
        // отримання об'єкта Intent, відправленого з попереднього актівіті -
        Intent intent = getIntent();

        // отримання даних з об'єкта Intent з використанням методу, getSerializableExtra -
        // використовується у разі передачі даних через модель, що імплементує інтерфейс Serializable
        order = (Order) intent.getSerializableExtra(ConstantsStore.KEY_ORDER);

        // встановлення первинного значення на радіобатон, що відповідає малому розміру піци
        order.setPizzaSize(PizzaSize.SMALL);
    }

    // метод підключення слухачів/подій (onClick) до елементів
    @Override
    public void onClick(View view) {
        int btn_id = view.getId();
        switch (btn_id) {
            case R.id.ivMinusMeat:
            case R.id.ivPlusMeat:
            case R.id.ivMinusMushroom:
            case R.id.ivPlusMushroom:
            case R.id.ivMinusCheese:
            case R.id.ivPlusCheese: {
                changeToppingsCount(btn_id);
                break;
            }
            case R.id.btnMakeOrder: {
                showOrderActivity();
                break;
            }
        }
    }

    // зміна кількості добавок до піци та приєднання/знищення добавки у колекції toppingCountList
    // в залежності від обраної кількості (0 - знищується, >0 - додається)
    private void changeToppingsCount(int btn_id) {
        switch (btn_id) {
            case R.id.ivMinusMeat: {
                changeAllToppingsCounter(tvCountMeat, false);
                break;
            }
            case R.id.ivPlusMeat: {
                changeAllToppingsCounter(tvCountMeat, true);
                break;
            }
            case R.id.ivMinusMushroom: {
                changeAllToppingsCounter(tvCountMushroom, false);
                break;
            }
            case R.id.ivPlusMushroom: {
                changeAllToppingsCounter(tvCountMushroom, true);
                break;
            }
            case R.id.ivMinusCheese: {
                changeAllToppingsCounter(tvCountCheese, false);
                break;
            }
            case R.id.ivPlusCheese: {
                changeAllToppingsCounter(tvCountCheese, true);
                break;
            }
        }

        // додавання добавок до колеції
        changeOrderToppingsList();
    }

    // додавання/виключення добавок до колеції
    private void changeOrderToppingsList() {
        order.getToppingCountList().clear();
        if (!tvCountMeat.getText().equals("0")) {
            ToppingCount toppingCount = new ToppingCount(PizzaTopping.MEAT, Integer.parseInt(tvCountMeat.getText().toString()));
            order.addToppingCount(toppingCount);
        }
        if (!tvCountMushroom.getText().equals("0")) {
            order.addToppingCount(new ToppingCount(PizzaTopping.MUSHROOMS, Integer.parseInt(tvCountMushroom.getText().toString())));
        }
        if (!tvCountCheese.getText().equals("0")) {
            order.addToppingCount(new ToppingCount(PizzaTopping.CHEESE, Integer.parseInt(tvCountCheese.getText().toString())));
        }
    }

    // зміна кількості добавки в залежності від обраної математичної операції (додати/відняти)
    private void changeAllToppingsCounter(TextView tvCounter, boolean isPlus) {
        int counter = Integer.parseInt(tvCounter.getText().toString());
        tvCounter.setText(isPlus ? (counter < 3 ? "" + (counter + 1) : "" + counter) : (counter > 0 ? "" + (counter - 1) : "" + counter));
    }

    // запуск наступного Актівіті, передача даних до нього з використанням моделі order
    private void showOrderActivity() {
        Intent intent = new Intent(this, OrderActivity.class);
        intent.putExtra(ConstantsStore.KEY_ORDER, order);
        startActivity(intent);
    }
}
