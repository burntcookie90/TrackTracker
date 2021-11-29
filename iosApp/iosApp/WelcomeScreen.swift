import SwiftUI
import shared

struct WelcomeScreen: ConnectedView {
    typealias S = WelcomeStore
    typealias M = WelcomeModel
    typealias E = WelcomeEvents
    typealias F = WelcomeEffects
    
    struct Props {
        let addCarMode: Bool
        let cars: [UiCar]
        let selectableYears: [Int]
        let onAddCar: () -> Void
        let onDiscardCreateCar: () -> Void
        let onCreateCar: (UiCar) -> Void
    }
    
    func map(state: WelcomeModel, dispatch: @escaping DispatchFunction<WelcomeEvents>) -> Props {
        return Props(
            addCarMode: state.addCarMode,
            cars: state.cars,
            selectableYears: state.selectableYears.map({ kInt in
                kInt.intValue
            }),
            onAddCar: { dispatch(WelcomeEvents.AddCar.shared)},
            onDiscardCreateCar: { dispatch(WelcomeEvents.DismissAddCarDialog.shared)},
            onCreateCar: { car in
                dispatch(WelcomeEvents.CreateCar(car: car))
            }
        )
    }
    
    func body(props: Props) -> some View {
        NavigationView {
            VStack {
                if (props.addCarMode) {
                    AddCarView(selectableYears: props.selectableYears, onDiscard: props.onDiscardCreateCar, onSubmit: props.onCreateCar)
                }
                else {
                    if (props.cars.isEmpty) {
                        addCarButton(onAddCar: props.onAddCar)
                    }
                    else {
                        ScrollView {
                            LazyVStack(alignment: .leading) {
                                ForEach(props.cars, id: \.self) { car in
                                    Text("\(car.titleDisplay())")
                                        .font(.title3)
                                    if let nickname = car.nickname {
                                        Text(nickname)
                                            .font(.subheadline)
                                    }
                                }
                            }
                        }
                        addCarButton(onAddCar: props.onAddCar)
                        
                    }
                }
            }
            .navigationTitle("Track Tracker")
        }
    }
    
    func addCarButton(onAddCar: @escaping () -> Void) -> some View {
        Button("Add a car") {
            onAddCar()
        }
    }
}

struct AddCarView: View {
    @State private var year: Int
    @State private var make: String = ""
    @State private var model: String = ""
    @State private var trim: String = ""
    @State private var nickname: String = ""
    
    let onDiscard: () -> Void
    let onSubmit: (UiCar) -> Void
    let selectableYears: [Int]
    
    init(selectableYears: [Int], onDiscard: @escaping () -> Void, onSubmit: @escaping (UiCar) -> Void) {
        self.selectableYears = selectableYears
        self.year = self.selectableYears.first ?? 2021
        self.onDiscard = onDiscard
        self.onSubmit = onSubmit
    }
    
    var body: some View {
        VStack {
            Form {
                Picker("Year", selection: $year) {
                    ForEach(selectableYears, id: \.self) {
                        Text(String($0))
                    }
                }
                .pickerStyle(DefaultPickerStyle())
                TextField("Make", text: $make)
                TextField("Model", text: $model)
                TextField("trim", text: $trim)
                TextField("nickname", text: $nickname)
            }
            HStack(spacing: 24) {
                Button("Discard") {
                    onDiscard()
                }
                Button("Submit") {
                    onSubmit(UiCar(year: Int32(year), make: make, model: model,
                                   trim: trim, nickname: nickname))
                }
            }
        }
    }
}
struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        WelcomeScreen()
    }
}
