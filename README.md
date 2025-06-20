# 🌿 SkinLab – Personalized Skincare Recommendation App

**SkinLab** is a hybrid e-commerce mobile app that curates skincare and makeup product recommendations based on user-selected filters such as skin concerns (e.g., acne scarring), product type, and budget. Built in Java using Android Studio, SkinLab focuses on usability, personalized filtering, and ethical data handling—avoiding invasive technologies like facial scanning.

---

## 🔍 Core Features

### ✅ Filter → Fetch → Display Results
- Custom filtering via `UserFilter` class.
- Retrofit integration (`ProductApiService`) to query an external API for product data.
- Results displayed using `RecyclerView` and `ProductAdapter`.

### 💾 Local Storage with Room
- `FavoritesManager` (singleton) to manage in-app product lists.
- `Room` database integration via `ProductDao` for:
  - Favorites
  - Routine
  - To-Buy lists

### ⭐ Reviews & Ratings (Mocked)
- Displayed using `RatingBar` and static data.
- Future implementation will allow user-submitted reviews/ratings.

### 🧠 Minimal Access Authentication
- Simplified login using an access key (min. 8 characters).
- Includes input validation and error handling.

### 🎨 UI/UX
- Built using Material Design Components (`ChipGroup`, `RangeSlider`, `MaterialCardView`).
- Responsive, clean layout with `ScrollView` for better navigation.

---

## ⚙️ Tech Stack

- **Language:** Java  
- **Frameworks:** Android SDK  
- **Libraries:**  
  - Retrofit (API calls)  
  - Room (local database)  
  - Material Components (UI)  
- **Architecture:** Singleton (FavoritesManager), DAO  
- **Build System:** Gradle  

---

## 🚧 MVP Status

| Feature                                | Status     |
|----------------------------------------|------------|
| Filter & Fetch Products                | ✅ Done     |
| Favorite/Add to Lists                  | ✅ Done     |
| Mock Reviews & Ratings                 | ✅ Done     |
| Real Review System                     | ⏳ Planned  |
| Access Key Login                       | ✅ Done     |
| Modify GUI                             | ⏳ Planned  |
---

## 💼 Business Perspective

SkinLab offers a privacy-respecting solution for skincare discovery by:
- Avoiding facial scan tech in favor of user-defined filters.
- Targeting users aged 18–40, skincare-conscious and active on social media.
- Using a freemium model with potential monetization via:
  - Affiliate integrations
  - Sponsored reviews
  - Brand collaborations

### Funding Strategy:
- Angel investors
- EU innovation grants
- Strategic partnerships with skincare brands or dermatology clinics

  More details are included in the `/business_model` folder, please view the ![Skin Lab Entreprenourial Analysis](business_model/Skin_Lab_Business_Analysis.pdf) document.

---

## 📈 Future Plans

- Replace mock reviews with user-generated content
- Expand API coverage to include skincare-specific endpoints
- Integrate shopping features via external retailer APIs
- Run beta-testing with reward-based feedback
- Optimize GUI based on design flow

---

## 🧪 Screenshots & Wireframes

🖼️ *UI mockups and screen flows are included in the `/designs` folder.*

---

## 🤝 Contact

For collaboration, feedback, or contribution inquiries, feel free to reach out:

📧 theodoraanc@gmail.com 


🔗 https://www.linkedin.com/in/theodora-a-71087a341/  ·  https://github.com/Theolinez 

## 📜 License

This project is for educational and demo purposes.
