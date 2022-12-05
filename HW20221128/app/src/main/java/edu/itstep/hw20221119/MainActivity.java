package edu.itstep.hw20221119;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import edu.itstep.hw20221119.models.ConstantsStore;
import edu.itstep.hw20221119.models.Order;
import edu.itstep.hw20221119.models.PizzaRecipe;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // поля - змінні класу, що відповідають активним елементам Activity
    private TextView tvCountPizza;

    private ImageButton imageButtonMinus;
    private ImageButton imageButtonPlus;

    private ImageButton ibPizza_1;
    private ImageButton ibPizza_2;
    private ImageButton ibPizza_3;

    private ConstraintLayout clPizza_1;
    private ConstraintLayout clPizza_2;
    private ConstraintLayout clPizza_3;

    private Button btnShowActivitySize;

    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        // встановлення обраної раніше кількості одиниць піци обраного рецепта
        if (order.getPizzaCount() != 0) {
            tvCountPizza.setText(String.valueOf(order.getPizzaCount()));

            // виділення кольором ранішеобраного рецепта
            if (PizzaRecipe.PANCHETA_GORGONDZOLA == order.getPizzaRecipe()) {
                choosePizzaRecipe(R.id.clPizza_1);
            } else if (PizzaRecipe.MEAT_PIZZA == order.getPizzaRecipe()) {
                choosePizzaRecipe(R.id.clPizza_2);
            } else if (PizzaRecipe.PHILADELPHIA == order.getPizzaRecipe()) {
                choosePizzaRecipe(R.id.clPizza_3);
            }
        }
    }

    // ініціалізація змінних
    private void initView() {
        tvCountPizza = findViewById(R.id.tvCountPizza);
        imageButtonMinus = findViewById(R.id.ibMinusMeat);
        imageButtonPlus = findViewById(R.id.ibPlusMeat);

        ibPizza_1 = findViewById(R.id.ibPizza_1);
        ibPizza_2 = findViewById(R.id.ibPizza_2);
        ibPizza_3 = findViewById(R.id.ibPizza_3);

        clPizza_1 = findViewById(R.id.clPizza_1);
        clPizza_2 = findViewById(R.id.clPizza_2);
        clPizza_3 = findViewById(R.id.clPizza_3);

        btnShowActivitySize = findViewById(R.id.btnShowActivitySize);
    }

    // підключення слухачів/подій до активних елементів
    // оскільки клас імплементує інтерфейс View.OnClickListener,
    // використовується посилання на метод -
    // усі події підключаються в одному методі onClick(), де з використанням
    // перемикача switch підключаються до конкретного елемента
    private void setListener() {
        imageButtonPlus.setOnClickListener(this);
        imageButtonMinus.setOnClickListener(this);

        ibPizza_1.setOnClickListener(this);
        ibPizza_2.setOnClickListener(this);
        ibPizza_3.setOnClickListener(this);
        clPizza_1.setOnClickListener(this);
        clPizza_2.setOnClickListener(this);
        clPizza_3.setOnClickListener(this);

        btnShowActivitySize.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int btn_id = view.getId();
        switch (btn_id) {
            case R.id.ibPlusMeat: { // зміна кількості замовленої піци (кнопка плюс)
                changeCountPizza(true);
                break;
            }
            case R.id.ibMinusMeat: { // зміна кількості замовленої піци (кнопка мінус)
                changeCountPizza(false);
                break;
            }
            case R.id.ibPizza_1:
            case R.id.ibPizza_2:
            case R.id.ibPizza_3:
            case R.id.clPizza_1:
            case R.id.clPizza_2:
            case R.id.clPizza_3: {
                choosePizzaRecipe(btn_id); // вибір рецепта (активні елементи - ImageButton, ConstraintLayout)
                break;
            }
            case R.id.btnShowActivitySize: { // перехід до наступного актівіті
                showPizzaSizeActivity();
                break;
            }
        }
    }

    // перехід до наступного актівіті при умові здійснення вибору рецепта піци та
    // кількості - кількість 1 встановлюється при виборі рецепта
    private void showPizzaSizeActivity() {
        if (tvCountPizza.getText().equals(tvCountPizzaStart())) {
            showChoosePizzaRecipeMassage(); // виведення тостового повідомлення про необхідність вибору рецепта піци
        } else {
            Intent intent = new Intent(this, PizzasizeActivity.class);
            intent.putExtra(ConstantsStore.KEY_ORDER, order); // приєднання моделі до об'єкта intent для передачі даних до наступного актівіті
            startActivity(intent);
        }
    }

    // вибір рецепта піци
    // при натиснення на активний елемент (ImageButton, ConstraintLayout) змінюється колір фону,
    // дані додаються до моделі, змінюється значення лічильника кількості (при першому виборі)
    private void choosePizzaRecipe(int btn_id) {
        // отримання та встановлення кольору - не використовується!!!
//        int notChooseColor = resources.getColor(R.color.cl_notChoose);
//        int ChooseColor = resources.getColor(R.color.cl_Choose);
//        clPizza_1.setBackgroundColor(ChooseColor);

        // отримання ресурсів для зміни кольору фону картки обраної піци
        Resources resources = getResources();
        Drawable drawableClChooseRecipe = resources.getDrawable(R.drawable.rectangle_rounded_choose_recipe);
        Drawable drawableClNotChooseRecipe = resources.getDrawable(R.drawable.rectangle_rounded);
        Drawable drawableIbChooseRecipe = resources.getDrawable(R.drawable.circle_ib_pizza_choose);
        Drawable drawableIbNotChooseRecipe = resources.getDrawable(R.drawable.circle_ib_pizza_notchoose);

        // зміна фону картки та додавання даних до моделі в залежності від використаного активного елементу
        switch (btn_id) {
            case R.id.ibPizza_1:
            case R.id.clPizza_1: {
                clPizza_1.setBackground(drawableClChooseRecipe);
                clPizza_2.setBackground(drawableClNotChooseRecipe);
                clPizza_3.setBackground(drawableClNotChooseRecipe);
                ibPizza_1.setBackground(drawableIbChooseRecipe);
                ibPizza_2.setBackground(drawableIbNotChooseRecipe);
                ibPizza_3.setBackground(drawableIbNotChooseRecipe);
                order.setPizzaRecipe(PizzaRecipe.PANCHETA_GORGONDZOLA);
                break;
            }
            case R.id.ibPizza_2:
            case R.id.clPizza_2: {
                clPizza_1.setBackground(drawableClNotChooseRecipe);
                clPizza_2.setBackground(drawableClChooseRecipe);
                clPizza_3.setBackground(drawableClNotChooseRecipe);
                ibPizza_1.setBackground(drawableIbNotChooseRecipe);
                ibPizza_2.setBackground(drawableIbChooseRecipe);
                ibPizza_3.setBackground(drawableIbNotChooseRecipe);
                order.setPizzaRecipe(PizzaRecipe.MEAT_PIZZA);
                break;
            }
            case R.id.ibPizza_3:
            case R.id.clPizza_3: {
                clPizza_1.setBackground(drawableClNotChooseRecipe);
                clPizza_2.setBackground(drawableClNotChooseRecipe);
                clPizza_3.setBackground(drawableClChooseRecipe);
                ibPizza_1.setBackground(drawableIbNotChooseRecipe);
                ibPizza_2.setBackground(drawableIbNotChooseRecipe);
                ibPizza_3.setBackground(drawableIbChooseRecipe);
                order.setPizzaRecipe(PizzaRecipe.PHILADELPHIA);
                break;
            }
        }
        // зміна кількості замовленої піци при першому виборі ("-" змінюється на 1)
        if (tvCountPizza.getText().equals(tvCountPizzaStart())) {
            order.setPizzaCount(1);
            tvCountPizza.setText("1");
        }
    }

    // зміна кількості замовлених одиниць при умові, що обрано конкретний рецепт
    // доступні значення лічильника - від 1 до нескінченності
    private void changeCountPizza(boolean isPlus) {
        if (!tvCountPizza.getText().toString().equals(tvCountPizzaStart())) {
            int countPizza = Integer.parseInt(tvCountPizza.getText().toString());
            countPizza = isPlus ? countPizza + 1 : countPizza > 1 ? countPizza - 1 : countPizza;
            order.setPizzaCount(countPizza);
            tvCountPizza.setText("" + countPizza);
        } else {
            showChoosePizzaRecipeMassage(); // виведення повідомлення про необхідність обрання рецепту
        }
    }

    // повертає первинне значення лічильника кількості одиниць замовлення
    // використовується для контролю чи обрано рецепт (саме первинне значення
    // може бути змінене у файлі строкоивх ресурсів, що не вплине на логіку)
    private String tvCountPizzaStart() {
        Resources resources = getResources();
        return resources.getString(R.string.tvCountPizza);
    }

    // виведення повідомлення про необхідність вибору рецепта
    private void showChoosePizzaRecipeMassage() {
        Resources resources = getResources();
        String message = resources.getString(R.string.messageChooseRecipe);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // ініціалізація моделі
    private void initData() {
        order = new Order();
    }

}