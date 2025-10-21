## 🚀 Start Launcher

1. Open the `.env` file and configure the following environment variables:
    - `CONTAINER_PHOTO_FOLDER` – Path to the photo directory inside the container
    - `HOST_PHOTO_FOLDER` – Path to the photo directory on the host machine

2. Start the service using Docker Compose:
   ```bash
   docker-compose up -d