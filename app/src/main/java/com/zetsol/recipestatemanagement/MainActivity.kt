package com.zetsol.recipestatemanagement

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.zetsol.recipestatemanagement.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView

    lateinit var appDatabase: AppDatabase

    private lateinit var recipeRepository: RecipeRepository
    private lateinit var recipeViewModel: RecipeViewModel

    private var recipesInserted = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNavigationView = binding.bottomNav

        // Initialize Room Database
        appDatabase = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app-database"
        ).build()

        recipeRepository = RecipeRepository(appDatabase.recipeDao())
        recipeViewModel = RecipeViewModel(recipeRepository)

        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.bottom_home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.bottom_fav -> {
                    replaceFragment(FavoritesFragment())
                    true
                }
                R.id.bottom_profile -> {
                    replaceFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }
        replaceFragment(HomeFragment())

//        if(recipeViewModel.allRecipes.value.isNullOrEmpty()) {   // Check if recipes have already been added
//            insertRecipes(recipeViewModel)
//        }

//        if (!recipesInserted) {
//            insertRecipes(recipeViewModel)
//            recipesInserted = true
//        }

        // Check if recipes have been inserted, if not, insert them
        if (!recipesInserted()) {
            insertRecipes(recipeViewModel)
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
    }

    private fun insertRecipes(recipeViewModel: RecipeViewModel) {

        val recipes = mutableListOf<Recipe>()
        val biryani = Recipe(
            name = "Biryani",
            ingredients = "Oil, Ginger Garlic paste, Chicken: 1Kg, Whole Spices, Powder spices: red chili powder, turmeric, cumin powder, coriander powder, salt, " +
                    "Chicken powder, Rice, Vegetables: sliced tomatoes, chopped coriander, mint leaves, and stem off green chilies, fried onions, Dried Plum (Aloo Bukhara) ",
            instructions = "Start your biryani process by making the biryani masala/curry/korma. First wash your chicken properly and set aside in a strainer. On high flame, add oil, ginger garlic paste, and chicken in a large pot. then add shan Bombay biryani mix , kashmiri red chili powder and chicken cube.\n" +
                    "\n" + "Mix for a minute or two. Now add Whole spices like Cloves, Green Cardamon, Cinnamon sticks and Whole Black Pepper or you can omit or add them according to your availability. " +
                    "\n Now add already soaked and drained rice and add 3 cups of water. \n add chopped coriander leaves, mint leaves, fried onions, dried plums, stem-off green chilies and thinly sliced tomatoes and toss them all together well. \n Now cook it for few minutes on high flame \n" +
                    "Now sprinke some food color and add green chilli, then cover the lid and steam(dum) it on low flame for 15-20 minutes.",
            cookingTime = "30-45 minutes", favorite = false, imageUrl = R.drawable.biryani
        )

        val chocolateCake = Recipe(
            name = "Chocolate Cake",
            ingredients = " flour, cocoa powder, baking powder, baking soda, and salt. This flour mixture should be sifted before adding it to the batter! oil, butter, eggs, vanilla extract, sour cream, and hot coffee. " +
                    "\nFrosting:  chopped chocolate, heavy cream, butter, vanilla, and just a touch of corn syrup. (needs to firm up in the refrigerator for at least an hour before being spread)",
            instructions = "First preheat your oven to 350 degrees (F).You’ll need two 9″ round pans for this recipe, plus parchment paper and baking spray for lining and coating them. \nOnce your pans are ready, you’ll sift those dry ingredients! Then you’ll beat your butter and sugars together until the mixture is light and fluffy. This will take about 4 minutes, " +
                    "and you may need to stop the mixer and scrape down the sides and bottom of the bowl once in a while! \nNext add the eggs, one at a time, and then add the vanilla and beat until combined. Toss in the dry ingredients, alternating with the oil and sour cream, and beat until just combined! You want to stop mixing when you can see the lightest trail of dry ingredients! And finally, pour in your hot coffee. Let the hot liquids sit, undisturbed, on top of the batter for 30 seconds, then stir until smooth!\n" +
                    "\n" +
                    "Divide the batter into the baking pans and bake for about 40 to 45 minutes. Then cook the cakes – in the pans – and on a wire rack for 20 minutes before removing the cakes from the pans. Then cool completely before frosting! " +
                    "\n Frosting: Place your chopped chocolate in a medium bowl. Boil your cream and corn syrup, then pour over the chopped chocolate! Pour the cream into the bowl and whisk smooth and glossy! Now, using an electric mixer, and beat in the butter on low to medium speed. Be sure to beat in the butter slowly, and mix until it’s fully incorporated into the frosting! Then pop it in the fridge until it’s firm enough to spread!" +
                    "\nDecor: Once your cakes are cooled and your frosting is firm, you’re ready to assemble!\n" +
                    "\n" +
                    "The first step is to cut each cake layer in half horizontally. It’s best to use a serrated knife for this task! You’ll also want to use the knife to serve off any domed parts on the tops of the cake. Set all of these cake scraps aside, we’ll use them later to decorate!\n" +
                    "\n" +
                    "Place one cake layer on a cake plate and spread about 1/2 cup of frosting on top of it, being sure it covers the surface completely. Top with another layer of cake, and repeat the frosting and layering process, ending with the last layer of cake on top. At this point, you’ll spread the remaining frosting over the top and sides of the cake. I like to use an offset spatula to do this!",
            cookingTime = "1 hour", favorite = false, imageUrl = R.drawable.chocolate_cake
        )

        val russianSalad = Recipe(
            name = "Russian Salad",
            ingredients = "6 Potatoes (Peeled), 1 Carrot ( To Taste), 4 Whole Eggs, 6 Large Pickles (Cut Into Cubes) 15 Ounce Canned Peas (Drained), half Cup Cubed Fully Cooked Meat (To Taste), 1 Tablespoon Chopped Fresh Dill (Optional)½ Cup Mayonnaise ( To Taste)",
            instructions = "Fill a large pot with water and bring it to a boil over medium-high heat.\nAdd the peeled potatoes to the boiling water. Let them boil for about 5 to 10 minutes. \nIntroduce the carrots and whole eggs to the pot with the potatoes. Continue boiling the mixture until the potatoes are tender, which usually takes an additional 10 to 15 minutes. \nDrain the water from the pot once the vegetables and eggs are cooked. Allow the mixture to cool slightly for easier handling. " +
                    "\nChop the boiled potatoes and carrots into small, bite-sized pieces. Carefully peel the boiled eggs and chop them as well. \nCombine the chopped potatoes, carrots, and eggs in a large mixing bowl. Add the cubed pickles, drained peas, cubed meat, and chopped dill to the bowl. \nStir in the mayonnaise with the other ingredients until the salad is evenly coated and creamy. \nOnce mixed, the Russian Salad is ready to be served. Optionally, you can refrigerate it for a while before serving to enhance its flavors.",
            cookingTime = "45 minutes", favorite = false, imageUrl = R.drawable.russian_salad
        )

        val chickenHandi = Recipe(
            name = "Chicken Handi",
            ingredients = "Boneless chicken, oil, Cream, Tomatoes, Dry Spices, Yogurt",
            instructions = "Fry chicken in oil on high flame till it changes colour. \nThen cut two tomatoes in half and let them steam in the pot on medium-low heat till you are able to peel off the skin with tongs. Then break the tomatoes with the spoon and mix in with chicken along with tomato paste. Let everything gel together and the oil to come on top. \nStir in all the dry spices. \nThen add the yogurt and cook it on medium-high flame. In the end pour the cooking cream. Let it thicken and add crushed dried fenugreek leaves. \n" +
                    "Serve this restaurant style Chicken Handi garnished with extra cream and alongside some Garlic Naan or plain boiled rice.",
            cookingTime = "30 minutes", favorite = false, imageUrl = R.drawable.chicken_handi
        )

        val whiteSaucePasta = Recipe(
            name = "White Sauce Pasta",
            ingredients = "Oil, Salt, Pasta, Chicken, Salt, Pepper powder, Vegetables, Garlic, Flour, Milk, Chilli Flakes, Oregano",
            instructions = "Add 1 spoon oil in boiling water, add salt to taste. \nAdd pasta 1 cup. Cook until boil. \nKeep Aside. \nButter 1 spoon, add half cup chicken in it. Add salt and pepper powder to taste. Cook for 5 minutes. \nAdd vegetables and cook for 2 minutes. \nKeep aside. \nTake 2 tbsp of butter and add garlic. Saute for 1 minute. \nAdd flour 1 tbsp. Cook it until its color changes to golden brown \nAdd milk 1 cup and keep stirring. Add salt and pepper powder to taste. Add half tsp of chilli flakes. Add oregano to taste. " +
                    "\nAdd pasta in it. Add chicken and vegetables. \nMix everything well",
            cookingTime = "30 minutes", favorite = false, imageUrl = R.drawable.white_sauce_pasta
        )

        val bhindi = Recipe(
            name = "Bhindi",
            ingredients = "Oil, Onion, Zeera, Bhindi, Salt, Haldi, Dhaniya powder, Pepper powder, Chilli powder, Imli, Green chillies, lemon",
            instructions = "Take some oil, heat it and add onions and zeera. \nAfter the onion turned reddish, add bhindi and salt. \nThen add Haldi, Dhaniya powder, Pepper powder and Chilli powder. \nNow add Imli and Green chillies. \nIn the last, squeeze some lemon on it. ",
            cookingTime = "20 minutes", favorite = false, imageUrl = R.drawable.bhindi
        )

        recipes.add(biryani)
        recipes.add(chocolateCake)
        recipes.add(russianSalad)
        recipes.add(chickenHandi)
        recipes.add(whiteSaucePasta)
        recipes.add(bhindi)

        for (recipe in recipes) {
            recipeViewModel.insert(recipe)
            Log.d("InsertRecipes", "Inserted recipe: ${recipe.name}")
        }

        // Update SharedPreferences to indicate that recipes have been inserted
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("recipesInserted", true)
        editor.apply()

    }

    private fun recipesInserted(): Boolean {
        // Check if recipes have been inserted by reading from SharedPreferences
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("recipesInserted", false)
    }
}
