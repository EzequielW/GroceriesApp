package com.example.listasupermercado;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.example.listasupermercado.adapter.SearchAdapter;
import com.example.listasupermercado.model.Category;
import com.example.listasupermercado.model.Product;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        ArrayList<Product> fruits = new ArrayList<>();
        fruits.add(new Product(getResources().getResourceEntryName(R.drawable.apple), getResources().getString(R.string.apple)));
        fruits.add(new Product(getResources().getResourceEntryName(R.drawable.banana), getResources().getString(R.string.banana)));
        fruits.add(new Product(getResources().getResourceEntryName(R.drawable.cherry), getResources().getString(R.string.cherry)));
        fruits.add(new Product(getResources().getResourceEntryName(R.drawable.avocado), getResources().getString(R.string.avocado)));
        fruits.add(new Product(getResources().getResourceEntryName(R.drawable.kiwi), getResources().getString(R.string.kiwi)));
        fruits.add(new Product(getResources().getResourceEntryName(R.drawable.strawberry), getResources().getString(R.string.strawberry)));
        fruits.add(new Product(getResources().getResourceEntryName(R.drawable.peach), getResources().getString(R.string.peach)));

        ArrayList<Product> vegetables = new ArrayList<>();
        vegetables.add(new Product(getResources().getResourceEntryName(R.drawable.asparagus), getResources().getString(R.string.asparagus)));
        vegetables.add(new Product(getResources().getResourceEntryName(R.drawable.broccoli), getResources().getString(R.string.broccoli)));
        vegetables.add(new Product(getResources().getResourceEntryName(R.drawable.onion), getResources().getString(R.string.onion)));
        vegetables.add(new Product(getResources().getResourceEntryName(R.drawable.paprika), getResources().getString(R.string.paprika)));
        vegetables.add(new Product(getResources().getResourceEntryName(R.drawable.carrot), getResources().getString(R.string.carrot)));
        vegetables.add(new Product(getResources().getResourceEntryName(R.drawable.garlic), getResources().getString(R.string.garlic)));
        vegetables.add(new Product(getResources().getResourceEntryName(R.drawable.cucumber), getResources().getString(R.string.cucumber)));
        vegetables.add(new Product(getResources().getResourceEntryName(R.drawable.lettuce), getResources().getString(R.string.lettuce)));
        vegetables.add(new Product(getResources().getResourceEntryName(R.drawable.cabbage), getResources().getString(R.string.cabbage)));
        vegetables.add(new Product(getResources().getResourceEntryName(R.drawable.leek), getResources().getString(R.string.leek)));
        vegetables.add(new Product(getResources().getResourceEntryName(R.drawable.eggplant), getResources().getString(R.string.eggplant)));
        vegetables.add(new Product(getResources().getResourceEntryName(R.drawable.celery), getResources().getString(R.string.celery)));

        ArrayList<Product> dairy = new ArrayList<>();
        dairy.add(new Product(getResources().getResourceEntryName(R.drawable.milk), getResources().getString(R.string.milk)));
        dairy.add(new Product(getResources().getResourceEntryName(R.drawable.cheese), getResources().getString(R.string.cheese)));

        ArrayList<Product> protein = new ArrayList<>();
        protein.add(new Product(getResources().getResourceEntryName(R.drawable.steak), getResources().getString(R.string.steak)));
        protein.add(new Product(getResources().getResourceEntryName(R.drawable.fish_food), getResources().getString(R.string.fish)));
        protein.add(new Product(getResources().getResourceEntryName(R.drawable.eggs), getResources().getString(R.string.eggs)));
        protein.add(new Product(getResources().getResourceEntryName(R.drawable.bacon), getResources().getString(R.string.bacon)));
        protein.add(new Product(getResources().getResourceEntryName(R.drawable.prawn), getResources().getString(R.string.prawn)));
        protein.add(new Product(getResources().getResourceEntryName(R.drawable.thanksgiving), getResources().getString(R.string.chicken)));
        protein.add(new Product(getResources().getResourceEntryName(R.drawable.crab), getResources().getString(R.string.crab)));
        protein.add(new Product(getResources().getResourceEntryName(R.drawable.rack_of_lamb), getResources().getString(R.string.lamb)));

        ArrayList<Product> grains = new ArrayList<>();
        grains.add(new Product(getResources().getResourceEntryName(R.drawable.bread), getResources().getString(R.string.bread)));
        grains.add(new Product(getResources().getResourceEntryName(R.drawable.corn), getResources().getString(R.string.corn)));
        grains.add(new Product(getResources().getResourceEntryName(R.drawable.rice_bowl), getResources().getString(R.string.rice)));
        grains.add(new Product(getResources().getResourceEntryName(R.drawable.spaghetti), getResources().getString(R.string.spaghetti)));
        grains.add(new Product(getResources().getResourceEntryName(R.drawable.potato), getResources().getString(R.string.potato)));
        grains.add(new Product(getResources().getResourceEntryName(R.drawable.sack_of_flour), getResources().getString(R.string.flour)));
        grains.add(new Product(getResources().getResourceEntryName(R.drawable.french_fries), getResources().getString(R.string.french_fries)));

        ArrayList<Product> drinks = new ArrayList<>();
        drinks.add(new Product(getResources().getResourceEntryName(R.drawable.beer_bottle), getResources().getString(R.string.beer)));
        drinks.add(new Product(getResources().getResourceEntryName(R.drawable.tea), getResources().getString(R.string.tea)));
        drinks.add(new Product(getResources().getResourceEntryName(R.drawable.coffee_pot), getResources().getString(R.string.coffee)));
        drinks.add(new Product(getResources().getResourceEntryName(R.drawable.wine_glass), getResources().getString(R.string.wine)));
        drinks.add(new Product(getResources().getResourceEntryName(R.drawable.beer_can), getResources().getString(R.string.beer_can)));

        ArrayList<Product> cleaning = new ArrayList<>();
        cleaning.add(new Product(getResources().getResourceEntryName(R.drawable.sponge), getResources().getString(R.string.soap)));
        cleaning.add(new Product(getResources().getResourceEntryName(R.drawable.clothes_detergent), getResources().getString(R.string.clothes_detergent)));
        cleaning.add(new Product(getResources().getResourceEntryName(R.drawable.toilet_paper), getResources().getString(R.string.toilet_paper)));
        cleaning.add(new Product(getResources().getResourceEntryName(R.drawable.sponge), getResources().getString(R.string.sponge)));
        cleaning.add(new Product(getResources().getResourceEntryName(R.drawable.dish_detergent), getResources().getString(R.string.dish_detergent)));
        cleaning.add(new Product(getResources().getResourceEntryName(R.drawable.bleach), getResources().getString(R.string.bleach)));
        cleaning.add(new Product(getResources().getResourceEntryName(R.drawable.paper_towel), getResources().getString(R.string.paper_towel)));
        cleaning.add(new Product(getResources().getResourceEntryName(R.drawable.cleaning_brush), getResources().getString(R.string.brush)));

        ArrayList<Product> sauces = new ArrayList<>();
        sauces.add(new Product(getResources().getResourceEntryName(R.drawable.ketchup), getResources().getString(R.string.ketchup)));
        sauces.add(new Product(getResources().getResourceEntryName(R.drawable.mayonnaise), getResources().getString(R.string.mayonnaise)));
        sauces.add(new Product(getResources().getResourceEntryName(R.drawable.mustard), getResources().getString(R.string.mustard)));
        sauces.add(new Product(getResources().getResourceEntryName(R.drawable.soy_sauce), getResources().getString(R.string.soy_sauce)));
        sauces.add(new Product(getResources().getResourceEntryName(R.drawable.sriracha), getResources().getString(R.string.sriracha)));
        sauces.add(new Product(getResources().getResourceEntryName(R.drawable.bbq_sauce), getResources().getString(R.string.bbq_sauce)));
        sauces.add(new Product(getResources().getResourceEntryName(R.drawable.worcestershire), getResources().getString(R.string.worcestershire)));

        ArrayList<Product> seasoning = new ArrayList<>();
        seasoning.add(new Product(getResources().getResourceEntryName(R.drawable.salt_shaker), getResources().getString(R.string.salt)));
        seasoning.add(new Product(getResources().getResourceEntryName(R.drawable.sugar), getResources().getString(R.string.sugar)));
        seasoning.add(new Product(getResources().getResourceEntryName(R.drawable.olive_oil), getResources().getString(R.string.oil)));
        seasoning.add(new Product(getResources().getResourceEntryName(R.drawable.pepper_shaker), getResources().getString(R.string.pepper)));
        seasoning.add(new Product(getResources().getResourceEntryName(R.drawable.honey), getResources().getString(R.string.honey)));
        seasoning.add(new Product(getResources().getResourceEntryName(R.drawable.lemon_juice), getResources().getString(R.string.lemon_juice)));

        ArrayList<Category> searchList = new ArrayList<>();
        searchList.add(new Category(1, getResources().getString(R.string.fruits), fruits));
        searchList.add(new Category(2, getResources().getString(R.string.vegetables), vegetables));
        searchList.add(new Category(3, getResources().getString(R.string.dairy), dairy));
        searchList.add(new Category(4, getResources().getString(R.string.proteins), protein));
        searchList.add(new Category(5, getResources().getString(R.string.grains), grains));
        searchList.add(new Category(6, getResources().getString(R.string.drinks), drinks));
        searchList.add(new Category(7, getResources().getString(R.string.cleaning), cleaning));
        searchList.add(new Category(8, getResources().getString(R.string.sauces), sauces));
        searchList.add(new Category(9, getResources().getString(R.string.seasoning), seasoning));

        final RecyclerView recyclerView = findViewById(R.id.search_menu_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        SearchAdapter mAdapter = new SearchAdapter(searchList);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);
    }
}
