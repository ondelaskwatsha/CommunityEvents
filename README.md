# Community Events

A small Android app that lets neighbours discover and share community events.

## Purpose

Community Events helps people find local events nearby and share their own events with the neighbourhood. Users browse live events pulled from the Ticketmaster Discovery API and add their own events, which are stored in Firebase Firestore.

## Team

- **AvuyileOD** — REST API integration, Add Event screen, bottom navigation
- **LithaAdam** — Sign-in screen, Profile screen, Firebase setup
- **ondelaskwatsha** — Project setup, Profile and location features

## Technologies Used

| Technology | Purpose |
|---|---|
| Jetpack Compose | Modern declarative UI |
| Navigation Compose | In-app navigation between screens |
| Retrofit + Gson | REST client for the Ticketmaster Discovery API |
| Firebase Firestore | NoSQL database for user-submitted events |
| Firebase Storage | Object/blob storage for event images |
| Coil | Async image loading from URLs |
| Material 3 | UI theming and components |

## Setup

### Prerequisites

- Android Studio (Koala or later)
- Android SDK 34+
- JDK 17+
- A free Ticketmaster developer account

### Getting a Ticketmaster API Key

1. Sign up at https://developer.ticketmaster.com
2. Go to **My Apps** and open the default application
3. Copy the **Consumer Key**

### Running the Project

1. Clone this repository:

2. Open the folder in Android Studio.
3. Add your API key to `local.properties` (in the project root, not inside `app/`):

4. Sync Gradle.
5. Run on an emulator or physical device.

### Firebase Setup (optional, for saving events)

Without Firebase, the app still runs but saving events shows a graceful error.

1. Create a project at https://console.firebase.google.com
2. Enable **Firestore Database** and **Storage**.
3. Add an Android app with package name `com.example.communityevents`.
4. Download `google-services.json` and place it inside the `app/` folder.

## Features

- Sign-in screen with email and phone validation
- Bottom navigation (Explore / Saved / Add event / Profile)
- Browse live events from the Ticketmaster Discovery API
- Search by country (`ZA`) and city
- Add new events to Firestore
- Input validation:
- Event name ≥ 3 characters
- Date must be valid `yyyy/mm/dd` and not in the past
- Description ≥ 10 characters
- Error handling for network failures, invalid API key, and rate limits

## REST API Integration

**Endpoint:** `GET https://app.ticketmaster.com/discovery/v2/events.json`

**Query parameters:**

| Parameter | Value | Purpose |
|---|---|---|
| `apikey` | from `local.properties` | Authentication |
| `countryCode` | `ZA` | Filter to South Africa |
| `city` | `Johannesburg` | Narrow to a specific city |
| `sort` | `date,asc` | Earliest events first |
| `size` | `20` | Results per page |

**Response mapping:**

| Ticketmaster JSON | Kotlin field |
|---|---|
| `_embedded.events[]` | `TicketmasterResponse.embedded.events` |
| `events[].name` | `EventDto.name` |
| `events[].dates.start.localDate` | `EventDto.dates.start.localDate` |
| `events[]._embedded.venues[0].name` | `EventDto.venueEmbed.venues[0].name` |
| `events[].images[0].url` | `EventDto.images[0].url` |

## Screenshots

![Sign-in screen](screenshots/sign-in.png)

![Explore events](screenshots/explore.png)

![Add event form](screenshots/add-event.png)

![Validation error](screenshots/validation.png)


## License

Class exercise — educational use only.