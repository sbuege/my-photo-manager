import os
import requests

# === CONFIGURATION ===
PIXABAY_API_KEY = "12161072-f434bc65ed04ec228861905c2"
DOWNLOAD_DIR = ""
IMAGE_COUNT = 25

def download_images(save_dir, category):
    if not os.path.exists(save_dir):
        os.makedirs(save_dir)

    category_folder = os.path.join(DOWNLOAD_DIR, category)
    if not os.path.exists(category_folder):
        os.makedirs(category_folder)

    results = search_pixabay(category, per_page=25)

    for i, hit in enumerate(results["hits"]):
        image_url = hit["largeImageURL"]
        image_data = requests.get(image_url).content
        filename = os.path.join(category_folder, f"{hit['id']}_{i}.jpg")

        with open(filename, "wb") as f:
            f.write(image_data)
        print(f"Downloaded: {filename}")

def search_pixabay(category, per_page):
    url = "https://pixabay.com/api/"
    params = {
        "key": PIXABAY_API_KEY,
        "category": category,
        "per_page": per_page,
        "image_type": "photo",
        "safesearch": "true"
    }

    response = requests.get(url, params=params)
    response.raise_for_status()
    return response.json()

def main():
    download_images(DOWNLOAD_DIR, "nature")
    download_images(DOWNLOAD_DIR, "animals")

if __name__ == "__main__":
    main()