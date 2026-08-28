import json
import time
import requests

# ==============================
# CONFIGURATION
# ==============================

API_KEY = "4c6a102811edd4a6abfbfbd1f275826f"

INPUT_FILE = "movie.json"
OUTPUT_FILE = "movie_with_posters.json"

TMDB_BASE_URL = "https://api.themoviedb.org/3"
POSTER_BASE_URL = "https://image.tmdb.org/t/p/w500"

# ==============================
# LOAD MOVIES
# ==============================

with open(INPUT_FILE, "r", encoding="utf-8") as file:
    movies = json.load(file)

print(f"Total movies found: {len(movies)}")

# ==============================
# PROCESS MOVIES
# ==============================

for index, movie in enumerate(movies, start=1):

    movie_id = movie.get("id")
    title = movie.get("title")

    print(f"[{index}/{len(movies)}] {title}")

    if not movie_id:
        movie["poster_path"] = None
        movie["poster_url"] = None
        continue

    url = f"{TMDB_BASE_URL}/movie/{movie_id}"

    params = {
        "api_key": API_KEY
    }

    try:
        response = requests.get(url, params=params, timeout=10)

        if response.status_code == 200:

            data = response.json()

            poster_path = data.get("poster_path")

            if poster_path:
                movie["poster_path"] = poster_path
                movie["poster_url"] = POSTER_BASE_URL + poster_path

                print(f"    Poster found")

            else:
                movie["poster_path"] = None
                movie["poster_url"] = None

                print(f"    No poster")

        else:
            movie["poster_path"] = None
            movie["poster_url"] = None

            print(f"    TMDB error: {response.status_code}")

    except requests.exceptions.RequestException as e:

        movie["poster_path"] = None
        movie["poster_url"] = None

        print(f"    Request failed: {e}")

    # Small delay to avoid hammering the API
    time.sleep(0.1)

# ==============================
# SAVE NEW JSON
# ==============================

with open(OUTPUT_FILE, "w", encoding="utf-8") as file:

    json.dump(
        movies,
        file,
        indent=2,
        ensure_ascii=False
    )

print()
print("================================")
print("DONE!")
print("================================")
print(f"Output file: {OUTPUT_FILE}")