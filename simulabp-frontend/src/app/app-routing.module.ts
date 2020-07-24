import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { DashboardComponent } from './components/dashboard/dashboard.component';
import { SimulazioneComponent } from './components/simulazione/simulazione.component';
import { BpmComponent } from './components/bpm/bpm.component';
import { BpmnComponent } from './components/bpmn/bpmn.component';
import { SimulatoreComponent } from './components/simulatore/simulatore.component';

const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent},
  { path: 'simulazione/:id', component: SimulazioneComponent},
  { path: 'bpm', component: BpmComponent},
  { path: 'bpmn', component: BpmnComponent},
  { path: 'simulatore', component: SimulatoreComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
