# EduHub-Frontend

![React](https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB)
![TypeScript](https://img.shields.io/badge/TypeScript-3178C6?style=for-the-badge&logo=typescript&logoColor=white)
![Vite](https://img.shields.io/badge/Vite-646CFF?style=for-the-badge&logo=vite&logoColor=white)
![Tailwind CSS](https://img.shields.io/badge/Tailwind%20CSS-06B6D4?style=for-the-badge&logo=tailwindcss&logoColor=white)
![Node.js](https://img.shields.io/badge/Node.js-339933?style=for-the-badge&logo=nodedotjs&logoColor=white)


The frontend of the **EduHub** project provides a user interface for managing lessons and students. It is developed with **React** and **TypeScript** and uses **Vite** for a fast and modern development experience.

## Main Features
- Manage lessons and students.
- View past and upcoming lessons.
- Responsive interface for optimal viewing on all devices.
- User login and registration with data validation.
- Manage quizzes and questions.
- Manage courses and lessons.
- Track student attendance.

## Technologies Used
- **React**: Main library for building the user interface.
- **TypeScript**: Typed language for improving code quality.
- **Vite**: Lightweight and fast bundler.
- **Axios**: For API calls to the backend.
- **Tailwind CSS**: Framework CSS for a modern and responsive style.
- **ShadCN/UI**: Components for a modern and intuitive design.
- **React Router**: For route management and page navigation.

## System Requirements
- **Node.js**
- **npm** or **yarn**: For package management.
- Backend active: The backend of the project must be started and accessible on port **8000**.

## Project Setup
### 1. Clone the Repository
```bash
git clone https://github.com/Andreafusella/EduHubFrontEnd.git
cd EduHubFrontEnd
```

### 2. Install dependencies
```bash
npm i
```

### 3. Start the project
```bash
npm run dev
```

### The application will be accessible at the default URL:
```bash
http://localhost:5173/
```

### Project Structure

```plaintext
.
├── public/
│   ├── png/
│   │   ├── avatar/
│   │   │   └── ...png
│   │   ├── landing/
│   │   │   └── ...png
│   │   └── logo.png
│   └── svg/
│       └── ...svg
└── src/
    ├── components/
    │   ├── auth/
    │   │   └── Login.tsx
    │   ├── common/
    │   │   ├── dialog/
    │   │   │   └── ..tsx
    │   │   └── ...tsx
    │   ├── commonPlus/
    │   │   └── ...tsx
    │   ├── layout/
    │   │   └── ...tsx
    │   ├── pages/
    │   │   ├── administrator/
    │   │   │   └── ...tsx
    │   │   ├── student/
    │   │   │   └── ...tsx
    │   │   ├── teacher/
    │   │   │   └── ...tsx
    │   │   └── ...tsx
    │   └── ui/
    │       └── ...tsx
    ├── content/
    │   └── ...tsx
    ├── hooks/
    │   └── ..tsx
    ├── interface/
    │   └── ...ts
    ├── lib/
    │   └── ..ts
    ├── App.tsx
    ├── index.css
    └── main.tsx
```

### Testing
- For testing the project, you need to have the backend active and accessible on port 8000.
- You can use the following credentials to test the application:
  - **Email**: admin@gmail.com
  - **Password**: Password123

### Contact
- For any questions or suggestions, please contact me at:
  - **Email**: andrea55fusella@gmail.com
  - **LinkedIn**: [Andrea Fusella](www.linkedin.com/in/andrea-fusella)
